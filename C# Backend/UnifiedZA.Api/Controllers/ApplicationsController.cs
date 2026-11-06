using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using UnifiedZA.Api.Data;
using UnifiedZA.Api.Models;

namespace UnifiedZA.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ApplicationsController : ControllerBase
    {
        private static readonly string[] ValidStatuses = { "Pending", "Submitted", "Accepted", "Rejected" };
        private readonly DbHelper _db;

        public ApplicationsController(DbHelper db)
        {
            _db = db;
        }

        // GET api/applications/user/1 -> SELECT (drives "My Applications" table)
        [HttpGet("user/{userId:int}")]
        public IActionResult GetByUser(int userId)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = @"
                    SELECT a.ApplicationID, a.UserID, a.CourseID, c.CourseName,
                           i.Name AS InstitutionName, a.ApplicationDate, a.Status
                    FROM Applications a
                    INNER JOIN Courses c ON a.CourseID = c.CourseID
                    INNER JOIN Institutions i ON c.InstitutionID = i.InstitutionID
                    WHERE a.UserID = @UserID
                    ORDER BY a.ApplicationDate DESC";

                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@UserID", userId);

                var applications = new List<Application>();
                using var reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    applications.Add(MapApplication(reader));
                }

                return Ok(applications);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while fetching applications.", detail = ex.Message });
            }
        }

        // GET api/applications/user/1/stats -> SELECT aggregate (drives Dashboard cards)
        [HttpGet("user/{userId:int}/stats")]
        public IActionResult GetStats(int userId)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = @"
                    SELECT
                        COUNT(*) AS TotalApplications,
                        SUM(CASE WHEN Status = 'Pending' THEN 1 ELSE 0 END) AS Pending,
                        SUM(CASE WHEN Status = 'Accepted' THEN 1 ELSE 0 END) AS Accepted
                    FROM Applications
                    WHERE UserID = @UserID";

                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@UserID", userId);

                using var reader = cmd.ExecuteReader();
                reader.Read();

                var stats = new DashboardStats
                {
                    TotalApplications = reader.IsDBNull(reader.GetOrdinal("TotalApplications")) ? 0 : reader.GetInt32(reader.GetOrdinal("TotalApplications")),
                    Pending = reader.IsDBNull(reader.GetOrdinal("Pending")) ? 0 : reader.GetInt32(reader.GetOrdinal("Pending")),
                    Accepted = reader.IsDBNull(reader.GetOrdinal("Accepted")) ? 0 : reader.GetInt32(reader.GetOrdinal("Accepted"))
                };

                return Ok(stats);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while fetching stats.", detail = ex.Message });
            }
        }

        // POST api/applications -> INSERT
        [HttpPost]
        public IActionResult Create([FromBody] CreateApplicationRequest request)
        {
            if (request.UserID <= 0 || request.CourseID <= 0)
            {
                return BadRequest(new { message = "A valid UserID and CourseID are required." });
            }

            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string insertSql = @"
                    INSERT INTO Applications (UserID, CourseID, ApplicationDate, Status)
                    OUTPUT INSERTED.ApplicationID
                    VALUES (@UserID, @CourseID, CAST(GETDATE() AS DATE), 'Pending')";

                using var cmd = new SqlCommand(insertSql, connection);
                cmd.Parameters.AddWithValue("@UserID", request.UserID);
                cmd.Parameters.AddWithValue("@CourseID", request.CourseID);

                int newId = (int)cmd.ExecuteScalar();

                // Seed the first status-tracker stage for the new application
                const string stageSql = @"
                    INSERT INTO StatusHistory (ApplicationID, StageName, StageDate, IsComplete, StageOrder)
                    VALUES (@ApplicationID, 'Application Submitted', CAST(GETDATE() AS DATE), 1, 1)";
                using var stageCmd = new SqlCommand(stageSql, connection);
                stageCmd.Parameters.AddWithValue("@ApplicationID", newId);
                stageCmd.ExecuteNonQuery();

                return CreatedAtAction(nameof(GetByUser), new { userId = request.UserID }, new { applicationID = newId });
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while creating application.", detail = ex.Message });
            }
        }

        // PUT api/applications/5/status -> UPDATE
        [HttpPut("{id:int}/status")]
        public IActionResult UpdateStatus(int id, [FromBody] UpdateStatusRequest request)
        {
            if (!ValidStatuses.Contains(request.Status))
            {
                return BadRequest(new { message = $"Status must be one of: {string.Join(", ", ValidStatuses)}." });
            }

            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = "UPDATE Applications SET Status = @Status WHERE ApplicationID = @ApplicationID";
                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@Status", request.Status);
                cmd.Parameters.AddWithValue("@ApplicationID", id);

                int rowsAffected = cmd.ExecuteNonQuery();
                if (rowsAffected == 0)
                {
                    return NotFound(new { message = $"No application found with ID {id}." });
                }

                return Ok(new { message = "Status updated.", applicationID = id, status = request.Status });
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while updating status.", detail = ex.Message });
            }
        }

        // DELETE api/applications/5 -> DELETE
        [HttpDelete("{id:int}")]
        public IActionResult Delete(int id)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                // Remove dependent status-history rows first (FK constraint)
                using (var deleteHistory = new SqlCommand(
                    "DELETE FROM StatusHistory WHERE ApplicationID = @ApplicationID", connection))
                {
                    deleteHistory.Parameters.AddWithValue("@ApplicationID", id);
                    deleteHistory.ExecuteNonQuery();
                }

                using var deleteApp = new SqlCommand(
                    "DELETE FROM Applications WHERE ApplicationID = @ApplicationID", connection);
                deleteApp.Parameters.AddWithValue("@ApplicationID", id);

                int rowsAffected = deleteApp.ExecuteNonQuery();
                if (rowsAffected == 0)
                {
                    return NotFound(new { message = $"No application found with ID {id}." });
                }

                return Ok(new { message = "Application deleted.", applicationID = id });
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while deleting application.", detail = ex.Message });
            }
        }

        // GET api/applications/5/history -> SELECT (drives Status Tracker timeline)
        [HttpGet("{id:int}/history")]
        public IActionResult GetHistory(int id)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = @"
                    SELECT StageName, StageDate, IsComplete, StageOrder
                    FROM StatusHistory
                    WHERE ApplicationID = @ApplicationID
                    ORDER BY StageOrder";

                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@ApplicationID", id);

                var stages = new List<StatusStage>();
                using var reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    stages.Add(new StatusStage
                    {
                        StageName = reader.GetString(reader.GetOrdinal("StageName")),
                        StageDate = reader.IsDBNull(reader.GetOrdinal("StageDate")) ? null : reader.GetDateTime(reader.GetOrdinal("StageDate")),
                        IsComplete = reader.GetBoolean(reader.GetOrdinal("IsComplete")),
                        StageOrder = reader.GetInt32(reader.GetOrdinal("StageOrder"))
                    });
                }

                return Ok(stages);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while fetching status history.", detail = ex.Message });
            }
        }

        private static Application MapApplication(SqlDataReader reader) => new()
        {
            ApplicationID = reader.GetInt32(reader.GetOrdinal("ApplicationID")),
            UserID = reader.GetInt32(reader.GetOrdinal("UserID")),
            CourseID = reader.GetInt32(reader.GetOrdinal("CourseID")),
            CourseName = reader.GetString(reader.GetOrdinal("CourseName")),
            InstitutionName = reader.GetString(reader.GetOrdinal("InstitutionName")),
            ApplicationDate = reader.GetDateTime(reader.GetOrdinal("ApplicationDate")),
            Status = reader.GetString(reader.GetOrdinal("Status"))
        };
    }
}
