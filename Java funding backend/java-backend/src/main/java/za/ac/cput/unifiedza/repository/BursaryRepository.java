package za.ac.cput.unifiedza.repository;

import za.ac.cput.unifiedza.domain.Bursary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BursaryRepository extends JpaRepository<Bursary, Long> {

@Query("""
SELECT b FROM Bursary b
WHERE
(:householdIncome <= b.maxHouseholdIncome)
AND (:baseAps >= b.minApsScore)
AND (b.fieldOfStudy = 'ALL' OR LOWER(b.fieldOfStudy) = LOWER(:fieldOfStudy))
ORDER BY b.minApsScore DESC
""")
List<Bursary> findMatchingBursaries(
@Param("householdIncome") BigDecimal householdIncome,
@Param("baseAps") Integer baseAps,
@Param("fieldOfStudy") String fieldOfStudy
);
}