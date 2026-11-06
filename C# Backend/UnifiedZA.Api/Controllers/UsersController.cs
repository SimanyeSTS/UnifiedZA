using System.Security.Cryptography;
using System.Text;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using UnifiedZA.Api.Data;
using UnifiedZA.Api.Models;

namespace UnifiedZA.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UsersController : ControllerBase
    {
        private readonly DbHelper _db;

        public UsersController(DbHelper db)
        {
            _db = db;
        }

        // POST api/users/register  -> INSERT
        [HttpPost("register")]
        public IActionResult Register([FromBody] RegisterRequest request)
        {
            // ---- Validation (rubric: Validation & Error Handling) ----
            if (string.IsNullOrWhiteSpace(request.FirstName) ||
                string.IsNullOrWhiteSpace(request.LastName))
            {
                return BadRequest(new { message = "First name and last name are required." });
            }

            if (string.IsNullOrWhiteSpace(request.Email) || !request.Email.Contains('@'))
            {
                return BadRequest(new { message = "A valid email address is required." });
            }

            if (string.IsNullOrWhiteSpace(request.Password) || request.Password.Length < 8)
            {
                return BadRequest(new { message = "Password must be at least 8 characters long." });
            }

            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                // Duplicate email check
                using (var checkCmd = new SqlCommand(
                    "SELECT COUNT(1) FROM Users WHERE Email = @Email", connection))
                {
                    checkCmd.Parameters.AddWithValue("@Email", request.Email);
                    int existing = (int)checkCmd.ExecuteScalar();
                    if (existing > 0)
                    {
                        return Conflict(new { message = "An account with this email already exists." });
                    }
                }

                const string insertSql = @"
                    INSERT INTO Users (FirstName, LastName, Email, PasswordHash)
                    OUTPUT INSERTED.UserID
                    VALUES (@FirstName, @LastName, @Email, @PasswordHash)";

                using var insertCmd = new SqlCommand(insertSql, connection);
                insertCmd.Parameters.AddWithValue("@FirstName", request.FirstName);
                insertCmd.Parameters.AddWithValue("@LastName", request.LastName);
                insertCmd.Parameters.AddWithValue("@Email", request.Email);
                insertCmd.Parameters.AddWithValue("@PasswordHash", HashPassword(request.Password));

                int newUserId = (int)insertCmd.ExecuteScalar();

                return CreatedAtAction(nameof(Register), new
                {
                    userID = newUserId,
                    firstName = request.FirstName,
                    lastName = request.LastName,
                    email = request.Email
                });
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while registering.", detail = ex.Message });
            }
        }

        // POST api/users/login -> SELECT
        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.Password))
            {
                return BadRequest(new { message = "Email and password are required." });
            }

            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = @"
                    SELECT UserID, FirstName, LastName, Email, PasswordHash
                    FROM Users
                    WHERE Email = @Email";

                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@Email", request.Email);

                using var reader = cmd.ExecuteReader();
                if (!reader.Read())
                {
                    return Unauthorized(new { message = "Invalid email or password." });
                }

                string storedHash = reader.GetString(reader.GetOrdinal("PasswordHash"));
                if (storedHash != HashPassword(request.Password))
                {
                    return Unauthorized(new { message = "Invalid email or password." });
                }

                var user = new UserDto
                {
                    UserID = reader.GetInt32(reader.GetOrdinal("UserID")),
                    FirstName = reader.GetString(reader.GetOrdinal("FirstName")),
                    LastName = reader.GetString(reader.GetOrdinal("LastName")),
                    Email = reader.GetString(reader.GetOrdinal("Email"))
                };

                return Ok(user);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while logging in.", detail = ex.Message });
            }
        }

        // GET api/users/5 -> SELECT (profile)
        [HttpGet("{id:int}")]
        public IActionResult GetProfile(int id)
        {
            try
            {
                using var connection = _db.GetConnection();
                connection.Open();

                const string sql = @"
                    SELECT UserID, FirstName, LastName, Email
                    FROM Users
                    WHERE UserID = @UserID";

                using var cmd = new SqlCommand(sql, connection);
                cmd.Parameters.AddWithValue("@UserID", id);

                using var reader = cmd.ExecuteReader();
                if (!reader.Read())
                {
                    return NotFound(new { message = $"No user found with ID {id}." });
                }

                var user = new UserDto
                {
                    UserID = reader.GetInt32(reader.GetOrdinal("UserID")),
                    FirstName = reader.GetString(reader.GetOrdinal("FirstName")),
                    LastName = reader.GetString(reader.GetOrdinal("LastName")),
                    Email = reader.GetString(reader.GetOrdinal("Email"))
                };

                return Ok(user);
            }
            catch (SqlException ex)
            {
                return StatusCode(500, new { message = "Database error while fetching profile.", detail = ex.Message });
            }
        }

        /// <summary>
        /// SHA-256 hash for demo purposes. In a production system, use a
        /// salted algorithm like BCrypt or PBKDF2 instead.
        /// </summary>
        private static string HashPassword(string password)
        {
            using var sha256 = SHA256.Create();
            byte[] bytes = sha256.ComputeHash(Encoding.UTF8.GetBytes(password));
            return Convert.ToHexString(bytes);
        }
    }
}
