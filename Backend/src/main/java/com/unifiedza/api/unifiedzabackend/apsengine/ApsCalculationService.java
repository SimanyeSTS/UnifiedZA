// Simanye's territory
package com.unifiedza.api.unifiedzabackend.apsengine;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ApsCalculationService {

    private final SubjectRepository subjectRepository;
    private final ApsScaleRepository apsScaleRepository;

    // Constructor Injection
    public ApsCalculationService(SubjectRepository subjectRepository, ApsScaleRepository apsScaleRepository) {
        this.subjectRepository = subjectRepository;
        this.apsScaleRepository = apsScaleRepository;
    }

    public int calculateTotalAps(List<SubjectScores> submittedScores) {
        // Business Rule 2: Minimum Core Subject Requirement
        if (submittedScores.size() < 7) {
            throw new IllegalArgumentException("A valid APS calculation requires at least 7 subjects.");
        }

        boolean hasMath = false;
        boolean hasMathLit = false;
        boolean hasHomeLanguage = false;
        boolean hasFirstAdditional = false;

        List<Integer> validApsPoints = new ArrayList<>();

        for (SubjectScores score : submittedScores) {
            // Fetch the subject from the DB using Cebe/Kgalalelo's catalog logic (simulated by your repo)
            Subject subject = subjectRepository.findBySubjectCode(score.getSubjectCode())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid subject code: " + score.getSubjectCode()));

            String subjectName = subject.getSubjectName().toLowerCase();

            // Check Language Requirements
            if (subjectName.contains("home language") || subjectName.contains("hl")) {
                hasHomeLanguage = true;
            }
            if (subjectName.contains("first additional language") || subjectName.contains("fal")) {
                hasFirstAdditional = true;
            }

            // Business Rule 1: Mutually Exclusive Subjects Check
            if ((subjectName.contains("mathematics") || subjectName.contains("maths")) && !subjectName.contains("literacy")) {
                hasMath = true;
            }
            if (subjectName.contains("mathematical literacy") || subjectName.contains("math lit")) {
                hasMathLit = true;
            }
            if (hasMath && hasMathLit) {
                throw new IllegalArgumentException("Validation Error: A student cannot take both Mathematics and Mathematical Literacy.");
            }

            // Business Rule 3: Life Orientation Exclusion
            // We identify the LO subject and skip adding its points to the calculation array
            if (subjectName.contains("life orientation") || subjectName.contains("lo")) {
                continue;
            }

            // Database lookup for the 7-point scale conversion
            Integer apsPoint = apsScaleRepository.findApsPointsByPercentage(score.getPercentage())
                    .orElseThrow(() -> new IllegalArgumentException("No APS scale bracket found for percentage: " + score.getPercentage()));

            validApsPoints.add(apsPoint);
        }

        // Enforce the language rule strictly before returning
        if (!hasHomeLanguage || !hasFirstAdditional) {
            throw new IllegalArgumentException("Validation Error: Calculation must legally include at least one Home Language and one First Additional Language.");
        }

        // Sort the remaining APS points descending (highest to lowest)
        validApsPoints.sort(Collections.reverseOrder());

        // Sum the top 6 scores and return the final integer
        return validApsPoints.stream()
                .limit(6)
                .mapToInt(Integer::intValue)
                .sum();
    }
}