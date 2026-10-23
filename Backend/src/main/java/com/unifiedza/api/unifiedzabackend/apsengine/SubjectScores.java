// Simanye's territory
package com.unifiedza.api.unifiedzabackend.apsengine;

import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class SubjectScores {

    @NotBlank(message = "Subject code cannot be blank")
    private String subjectCode;

    @NotNull(message = "Percentage is required")
    @Min(value = 0, message = "Percentage cannot be less than 0")
    @Max(value = 100, message = "Percentage cannot be greater than 100")
    private Integer percentage;
}
