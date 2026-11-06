package za.ac.cput.unifiedza.service;

import za.ac.cput.unifiedza.domain.Sponsor;
import java.util.List;

public interface SponsorService {
List<Sponsor> getAllSponsors();
Sponsor createSponsor(Sponsor sponsor);
}