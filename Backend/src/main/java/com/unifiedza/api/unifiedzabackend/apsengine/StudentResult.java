package com.unifiedza.api.unifiedzabackend.apsengine;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "student_results")
public class StudentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id", length = 10)
    private Integer resultId;

    // Kept as a raw ID to decouple from the Auth subsystem during individual development
    @Column(name = "user_id", length = 10, nullable = false)
    private Integer userId;

    // Proper JPA mapping to your own Subject entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "percentage", length = 3, nullable = false)
    private Integer percentage;

    @Column(name = "exam_type", length = 20, nullable = false)
    private String examType;
}
