// Kgalalelo's territory
package com.unifiedza.api.unifiedzabackend.funding;
@Entity
public class Bursary {
@Id
@generatedvalue(strategy = GenerartionType.IDENTITY) 
  private Long ID;
  private String name;
  private String FieldOfStudy;
  private double minimumAverage;
  private double amount;
}
