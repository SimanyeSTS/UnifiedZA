package za.ac.cput.unifiedza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.unifiedza.domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
}

