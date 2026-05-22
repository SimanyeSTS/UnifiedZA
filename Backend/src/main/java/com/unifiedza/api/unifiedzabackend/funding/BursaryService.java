// Kgalelo's territory
package com.unifiedza.api.unifiedzabackend.funding;
@Service
public class BursaryService {
  private final Bursaryrepository repository;

  public BursaryService(BursaryRepository repository)
  this.reposistory = repsository;
}
public List<Bursary>getAll() { return reposistory.findAll(); }
public Bursary save(Bursary bursary { return repository.save(bursary); }
public void delete (Long id { repository.deleteById(id); }
}
