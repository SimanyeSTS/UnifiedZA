package za.ac.cput.unifiedza.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.unifiedza.domain.Bursary;
import za.ac.cput.unifiedza.repository.BursaryRepository;
import za.ac.cput.unifiedza.service.BursaryService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BursaryServiceImpl implements BursaryService {

private final BursaryRepository bursaryRepository;

@Autowired
public BursaryServiceImpl(BursaryRepository bursaryRepository) {
this.bursaryRepository = bursaryRepository;
}

@Override
public List<Bursary> findMatchingBursaries(BigDecimal householdIncome, Integer baseAps, String fieldOfStudy) {
if (fieldOfStudy != null) {
fieldOfStudy = fieldOfStudy.trim().toLowerCase();
}
return bursaryRepository.findMatchingBursaries(householdIncome, baseAps, fieldOfStudy);
}

@Override
public Bursary createBursary(Bursary bursary) {
return bursaryRepository.save(bursary);
}

@Override
public List<Bursary> getAllBursaries() {
return bursaryRepository.findAll();
}
}