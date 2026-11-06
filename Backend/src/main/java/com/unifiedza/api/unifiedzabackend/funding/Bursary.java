// Kgalalelo's territory
package com.unifiedza.api.unifiedzabackend.funding;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Bursary {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ID;
  private String name;
  private String FieldOfStudy;
  private double minimumAverage;
  private double amount;
}
