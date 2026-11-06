using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using UnifiedZA.Api.Data;
using UnifiedZA.Api.Models;

namespace UnifiedZA.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class SavedCoursesController : ControllerBase
    {
        private const int MaxSavedCourses = 3;
        private readonly DbHelper _db;

        public SavedCoursesController(DbHelper db)
        {
            _db = db;
        }

        // GET api/savedcourses/user/1 -> SELECT (drives "Saved Courses (Cart)")
        [HttpGet("user/{userId:int}")]
        public IActionResult GetByUser(int userId)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = @"
                    SELECT sc.SavedCourseID, sc.UserID, sc.CourseID, c.CourseName,
                           i.Name AS InstitutionName, c.APSRequirement, c.IntakeYear, sc.DateAdded
                    FROM SavedCourses sc
                    INNER JOIN Courses c ON sc.CourseID = c.CourseID
                    INNER JOIN Institutions i ON c.InstitutionID = i.InstitutionID
                    WHERE sc.UserID = @UserID
                    ORDER BY sc.DateAdded";

                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@UserID", userId);

                var saved = new List<SavedCourse>();
                using var reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    saved.Add(new SavedCourse
                    {
                        SavedCourseID = reader.GetInt32(reader.GetOrdinal("SavedCourseID")),
                        UserID = reader.GetInt32(reader.GetOrdinal("UserID")),
                        CourseID = reader.GetInt32(reader.GetOrdinal("CourseID")),
                        CourseName = reader.GetString(reader.GetOrdinal("CourseName")),
                        InstitutionName = reader.GetString(reader.GetOrdinal("InstitutionName")),
                        APSRequirement = reader.GetInt32(reader.GetOrdinal("APSRequirement")),
                        IntakeYear = reader.GetInt32(reader.GetOrdinal("IntakeYear")),
                        DateAdded = reader.GetDateTime(reader.GetOrdinal("DateAdded"))
                    });
                }

                return Ok(saved);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while fetching saved courses.", detail = ex.Message });
            }
        }

        // POST api/savedcourses -> INSERT (enforces max 3 business rule)
        [HttpPost]
        public IActionResult Add([FromBody] CreateApplicationRequest request)
        {
            if (request.UserID <= 0 || request.CourseID <= 0)
            {
                return BadRequest(new { message = "A valid UserID and CourseID are required." });
            }

            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                using (var countCmd = new SqlCommand(
                    "SELECT COUNT(1) FROM SavedCourses WHERE UserID = @UserID", connection))
                {
                    countCmd.Parameters.AddWithValue("@UserID", request.UserID);
                    int currentCount = (int)countCmd.ExecuteScalar();

                    if (currentCount >= MaxSavedCourses)
                    {
                        return BadRequest(new { message = $"Maximum application limit of {MaxSavedCourses} reached. Please remove a course to add another." });
                    }
                }

                using (var dupCmd = new SqlCommand(
                    "SELECT COUNT(1) FROM SavedCourses WHERE UserID = @UserID AND CourseID = @CourseID", connection))
                {
                    dupCmd.Parameters.AddWithValue("@UserID", request.UserID);
                    dupCmd.Parameters.AddWithValue("@CourseID", request.CourseID);
                    if ((int)dupCmd.ExecuteScalar() > 0)
                    {
                        return Conflict(new { message = "This course is already saved." });
                    }
                }

                const string insertSql = @"
                    INSERT INTO SavedCourses (UserID, CourseID, DateAdded)
                    OUTPUT INSERTED.SavedCourseID
                    VALUES (@UserID, @CourseID, GETDATE())";

                using var insertCmd = new SqlCommand(insertSql, connection);
                insertCmd.Parameters.AddWithValue("@UserID", request.UserID);
                insertCmd.Parameters.AddWithValue("@CourseID", request.CourseID);

                int newId = (int)insertCmd.ExecuteScalar();

                return CreatedAtAction(nameof(GetByUser), new { userId = request.UserID }, new { savedCourseID = newId });
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while saving course.", detail = ex.Message });
            }
        }

        // DELETE api/savedcourses/5 -> DELETE
        [HttpDelete("{id:int}")]
        public IActionResult Remove(int id)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = "DELETE FROM SavedCourses WHERE SavedCourseID = @SavedCourseID";
                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@SavedCourseID", id);

                int rowsAffected = cmd.ExecuteNonQuery();
                if (rowsAffected == 0)
                {
                    return NotFound(new { message = $"No saved course found with ID {id}." });
                }

                return Ok(new { message = "Removed from saved courses.", savedCourseID = id });
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while removing saved course.", detail = ex.Message });
            }
        }
    }
}
