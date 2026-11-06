using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using UnifiedZA.Api.Data;
using UnifiedZA.Api.Models;

namespace UnifiedZA.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CoursesController : ControllerBase
    {
        private readonly DbHelper _db;

        public CoursesController(DbHelper db)
        {
            _db = db;
        }

        // GET api/courses?search=IT  -> SELECT (with optional search filter)
        [HttpGet]
        public IActionResult GetAll([FromQuery] string? search)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                string sql = @"
                    SELECT c.CourseID, c.CourseName, c.InstitutionID, i.Name AS InstitutionName,
                           c.APSRequirement, c.IntakeYear
                    FROM Courses c
                    INNER JOIN Institutions i ON c.InstitutionID = i.InstitutionID";

                if (!string.IsNullOrWhiteSpace(search))
                {
                    sql += " WHERE c.CourseName LIKE @Search OR i.Name LIKE @Search";
                }
                sql += " ORDER BY c.CourseName";

                using var cmd = new SqlCommand(sql, connection);
                if (!string.IsNullOrWhiteSpace(search))
                {
                    cmd.Parameters.AddWithValue("@Search", $"%{search}%");
                }

                var courses = new List<Course>();
                using var reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    courses.Add(new Course
                    {
                        CourseID = reader.GetInt32(reader.GetOrdinal("CourseID")),
                        CourseName = reader.GetString(reader.GetOrdinal("CourseName")),
                        InstitutionID = reader.GetInt32(reader.GetOrdinal("InstitutionID")),
                        InstitutionName = reader.GetString(reader.GetOrdinal("InstitutionName")),
                        APSRequirement = reader.GetInt32(reader.GetOrdinal("APSRequirement")),
                        IntakeYear = reader.GetInt32(reader.GetOrdinal("IntakeYear"))
                    });
                }

                return Ok(courses);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while fetching courses.", detail = ex.Message });
            }
        }

        // GET api/courses/5 -> SELECT single
        [HttpGet("{id:int}")]
        public IActionResult GetById(int id)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = @"
                    SELECT c.CourseID, c.CourseName, c.InstitutionID, i.Name AS InstitutionName,
                           c.APSRequirement, c.IntakeYear
                    FROM Courses c
                    INNER JOIN Institutions i ON c.InstitutionID = i.InstitutionID
                    WHERE c.CourseID = @CourseID";

                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@CourseID", id);

                using var reader = cmd.ExecuteReader();
                if (!reader.Read())
                {
                    return NotFound(new { message = $"No course found with ID {id}." });
                }

                var course = new Course
                {
                    CourseID = reader.GetInt32(reader.GetOrdinal("CourseID")),
                    CourseName = reader.GetString(reader.GetOrdinal("CourseName")),
                    InstitutionID = reader.GetInt32(reader.GetOrdinal("InstitutionID")),
                    InstitutionName = reader.GetString(reader.GetOrdinal("InstitutionName")),
                    APSRequirement = reader.GetInt32(reader.GetOrdinal("APSRequirement")),
                    IntakeYear = reader.GetInt32(reader.GetOrdinal("IntakeYear"))
                };

                return Ok(course);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while fetching course.", detail = ex.Message });
            }
        }
    }
}
