package za.ac.cput.unifiedza.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.unifiedza.domain.Sponsor;
import za.ac.cput.unifiedza.repository.SponsorRepository;
import za.ac.cput.unifiedza.service.SponsorService;

import java.util.List;

@Service
public class SponsorServiceImpl implements SponsorService {

private final SponsorRepository sponsorRepository;

public SponsorServiceImpl(SponsorRepository sponsorRepository) {
this.sponsorRepository = sponsorRepository;
}

@Override
public List<Sponsor> getAllSponsors() {
return sponsorRepository.findAll();
}

@Override
public Sponsor createSponsor(Sponsor sponsor) {
if (sponsor.getName() == null || sponsor.getName().isBlank()) {
throw new IllegalArgumentException("Sponsor name cannot be empty.");
}
return sponsorRepository.save(sponsor);
}
}
