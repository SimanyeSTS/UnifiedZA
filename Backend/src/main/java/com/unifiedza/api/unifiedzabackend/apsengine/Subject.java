package com.unifiedza.api.unifiedzabackend.apsengine;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id", length = 10)
    private Integer subjectId;

    @Column(name = "subject_code", length = 10, nullable = false, unique = true)
    private String subjectCode;

    @Column(name = "subject_name", length = 50, nullable = false)
    private String subjectName;

    @Column(name = "is_mandatory")
    private Boolean isMandatory;
}
