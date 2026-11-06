package za.ac.cput.unifiedza.service;

import za.ac.cput.unifiedza.domain.Bursary;
import java.math.BigDecimal;
import java.util.List;

public interface BursaryService {
List<Bursary> findMatchingBursaries(BigDecimal householdIncome, Integer baseAps, String fieldOfStudy);
Bursary createBursary(Bursary bursary);
List<Bursary> getAllBursaries();
}  

