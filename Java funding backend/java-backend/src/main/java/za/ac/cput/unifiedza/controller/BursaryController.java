package za.ac.cput.unifiedza.controller;

import za.ac.cput.unifiedza.domain.Bursary;
import za.ac.cput.unifiedza.repository.BursaryRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bursaries")
@CrossOrigin(origins = "*")
public class BursaryController {

private final BursaryRepository bursaryRepository;

public BursaryController(BursaryRepository bursaryRepository) {
this.bursaryRepository = bursaryRepository;
}

@GetMapping("/match")
public List<Bursary> getMatchingBursaries(
@RequestParam BigDecimal householdIncome,
@RequestParam Integer baseAps,
@RequestParam(defaultValue = "ALL") String fieldOfStudy) {
return bursaryRepository.findMatchingBursaries(householdIncome, baseAps, fieldOfStudy);
}
}
