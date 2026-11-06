using UnifiedZA.Api.Data;

var builder = WebApplication.CreateBuilder(args);

// Register controllers (each Controller = one CRUD area: Users, Applications, Courses, SavedCourses)
builder.Services.AddControllers();

// DbHelper wraps the SqlConnection creation so every controller shares one
// consistent, testable way of talking to SQL Server.
builder.Services.AddSingleton<DbHelper>();

// Allow the HTML/CSS/JS frontend (served separately, e.g. from Live Server
// or a static file host) to call this API during development.
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowFrontend", policy =>
        policy.AllowAnyOrigin()
              .AllowAnyHeader()
              .AllowAnyMethod());
});

var app = builder.Build();

app.UseCors("AllowFrontend");
app.UseDefaultFiles();
app.UseStaticFiles();
app.MapControllers();

app.Run();
