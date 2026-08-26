package com.unifiedza.api.unifiedzabackend.apsengine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ApsScaleRepository extends JpaRepository<ApsScale, Integer> {
    @Query("SELECT a.apsPoints FROM ApsScale a WHERE :percentage >= a.minPercentage AND :percentage <= a.maxPercentage")
    Optional<Integer> findApsPointsByPercentage(@Param("percentage") Integer percentage);
}
