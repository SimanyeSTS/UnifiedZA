// Kgalelo's territory
package com.unifiedza.api.unifiedzabackend.funding;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BursaryService {
  private final BursaryRepository repository;

  public BursaryService(BursaryRepository repository) {
    this.repository = repository;
  }
  public List<Bursary> getAll() { return repository.findAll(); }
public Bursary save(Bursary bursary) { return repository.save(bursary); }
public void delete (Long id) { repository.deleteById(id); }
}
