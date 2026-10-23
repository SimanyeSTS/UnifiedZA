// Simanye's territory
package com.unifiedza.api.unifiedzabackend.apsengine;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/aps")
public class ApsController {

    private final ApsCalculationService apsCalculationService;

    public ApsController(ApsCalculationService apsCalculationService) {
        this.apsCalculationService = apsCalculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateAps(@Valid @RequestBody List<SubjectScores> submittedScores) {
        try {
            int finalAps = apsCalculationService.calculateTotalAps(submittedScores);
            return ResponseEntity.ok(finalAps);
        } catch (IllegalArgumentException e) {
            // If the student violates a business rule (like taking Math and Math Lit),
            // this returns the error message cleanly to the frontend.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}