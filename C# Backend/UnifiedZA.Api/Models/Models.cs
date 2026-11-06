namespace UnifiedZA.Api.Models
{
    // This is Chuma's subsystem

    //public class User
    //{
    //    public int UserID { get; set; }
    //    public string FirstName { get; set; } = string.Empty;
    //    public string LastName { get; set; } = string.Empty;
    //    public string Email { get; set; } = string.Empty;
    //    public string PasswordHash { get; set; } = string.Empty;
    //    public DateTime DateCreated { get; set; }
    //}

    // No hashing, user password is not safe, why did you not use System.Security.Cryptography

    /// <summary>Safe shape of a user to send back to the frontend (no password hash).</summary>
    public class UserDto
    {
        public int UserID { get; set; }
        public string FirstName { get; set; } = string.Empty;
        public string LastName { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
    }

    public class RegisterRequest
    {
        public string FirstName { get; set; } = string.Empty;
        public string LastName { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
        public string Password { get; set; } = string.Empty;
    }

    public class LoginRequest
    {
        public string Email { get; set; } = string.Empty;
        public string Password { get; set; } = string.Empty;
    }

    public class Institution
    {
        public int InstitutionID { get; set; }
        public string Name { get; set; } = string.Empty;
        public string Location { get; set; } = string.Empty;
    }

    public class Course
    {
        public int CourseID { get; set; }
        public string CourseName { get; set; } = string.Empty;
        public int InstitutionID { get; set; }
        public string InstitutionName { get; set; } = string.Empty;
        public int APSRequirement { get; set; }
        public int IntakeYear { get; set; }
    }

    public class Application
    {
        public int ApplicationID { get; set; }
        public int UserID { get; set; }
        public int CourseID { get; set; }
        public string CourseName { get; set; } = string.Empty;
        public string InstitutionName { get; set; } = string.Empty;
        public DateTime ApplicationDate { get; set; }
        public string Status { get; set; } = "Pending";
    }

    public class CreateApplicationRequest
    {
        public int UserID { get; set; }
        public int CourseID { get; set; }
    }

    public class UpdateStatusRequest
    {
        public string Status { get; set; } = string.Empty;
    }

    public class SavedCourse
    {
        public int SavedCourseID { get; set; }
        public int UserID { get; set; }
        public int CourseID { get; set; }
        public string CourseName { get; set; } = string.Empty;
        public string InstitutionName { get; set; } = string.Empty;
        public int APSRequirement { get; set; }
        public int IntakeYear { get; set; }
        public DateTime DateAdded { get; set; }
    }

    public class StatusStage
    {
        public string StageName { get; set; } = string.Empty;
        public DateTime? StageDate { get; set; }
        public bool IsComplete { get; set; }
        public int StageOrder { get; set; }
    }

    public class DashboardStats
    {
        public int TotalApplications { get; set; }
        public int Pending { get; set; }
        public int Accepted { get; set; }
    }
}
