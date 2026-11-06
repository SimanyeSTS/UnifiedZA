package za.ac.cput.unifiedza.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.unifiedza.domain.Sponsor;
import za.ac.cput.unifiedza.service.SponsorService;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
@CrossOrigin(origins = "*")
public class SponsorController {

private final SponsorService sponsorService;

public SponsorController(SponsorService sponsorService) {
this.sponsorService = sponsorService;
}

@GetMapping
public ResponseEntity<List<Sponsor>> getAllSponsors() {
return ResponseEntity.ok(sponsorService.getAllSponsors());
}

@PostMapping
public ResponseEntity<Sponsor> createSponsor(@RequestBody Sponsor sponsor) {
return new ResponseEntity<>(sponsorService.createSponsor(sponsor), HttpStatus.CREATED);
}
}