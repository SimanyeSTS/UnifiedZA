package za.ac.cput.unifiedza.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bursaries")
public class Bursary {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long bursaryId;

@ManyToOne
@JoinColumn(name = "sponsor_id", nullable = false)
private Sponsor sponsor;

@Column(nullable = false, length = 150)
private String title;

@Column(columnDefinition = "TEXT")
private String description;

private Boolean isNsfas = false;

@Column(nullable = false, precision = 18, scale = 2)
private BigDecimal maxHouseholdIncome;

@Column(nullable = false)
private Integer minApsScore;

@Column(length = 100)
private String fieldOfStudy = "ALL";

public Bursary() {}

public Long getBursaryId() { return bursaryId; }
public void setBursaryId(Long bursaryId) { this.bursaryId = bursaryId; }

public Sponsor getSponsor() { return sponsor; }
public void setSponsor(Sponsor sponsor) { this.sponsor = sponsor; }

public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }

public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }

public Boolean getIsNsfas() { return isNsfas; }
public void setIsNsfas(Boolean isNsfas) { this.isNsfas = isNsfas; }

public BigDecimal getMaxHouseholdIncome() { return maxHouseholdIncome; }
public void setMaxHouseholdIncome(BigDecimal maxHouseholdIncome) { this.maxHouseholdIncome = maxHouseholdIncome; }

public Integer getMinApsScore() { return minApsScore; }
public void setMinApsScore(Integer minApsScore) { this.minApsScore = minApsScore; }

public String getFieldOfStudy() { return fieldOfStudy; }
public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }
}