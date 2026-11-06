package za.unifiedza;

import java.util.List;

public record Institution(String id, String name, String shortName, String province,
        String city, String type, int aps, String description, String about,
        List<String> campuses, List<String> faculties, List<Course> courses, String website) {
    public record Course(String name, String faculty, int aps, String requirements) { }
}
