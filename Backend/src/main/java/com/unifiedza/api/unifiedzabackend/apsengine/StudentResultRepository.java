package com.unifiedza.api.unifiedzabackend.apsengine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentResultRepository extends JpaRepository<StudentResult, Integer> {
    List<StudentResult> findByUserId(Integer userId);
}
