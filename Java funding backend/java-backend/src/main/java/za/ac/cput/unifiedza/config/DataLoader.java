package za.ac.cput.unifiedza.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.ac.cput.unifiedza.domain.Bursary;
import za.ac.cput.unifiedza.repository.BursaryRepository;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

private final BursaryRepository bursaryRepository;

public DataLoader(BursaryRepository bursaryRepository) {
this.bursaryRepository = bursaryRepository;
}

@Override
public void run(String... args) {
if (bursaryRepository.count() == 0) {
Bursary nsfas = new Bursary();
nsfas.setTitle("NSFAS Financial Aid");
nsfas.setDescription("Government funding scheme for undergraduate students.");
nsfas.setMinApsScore(20);
nsfas.setMaxHouseholdIncome(new BigDecimal("350000"));
nsfas.setFieldOfStudy("ALL");
nsfas.setIsNsfas(true);

Bursary techBursary = new Bursary();
techBursary.setTitle("CPUT ICT Sector Bursary");
techBursary.setDescription("Funding for software development and IT majors.");
techBursary.setMinApsScore(28);
techBursary.setMaxHouseholdIncome(new BigDecimal("600000"));
techBursary.setFieldOfStudy("information technology");
techBursary.setIsNsfas(false);

bursaryRepository.save(nsfas);
bursaryRepository.save(techBursary);
}
}
}