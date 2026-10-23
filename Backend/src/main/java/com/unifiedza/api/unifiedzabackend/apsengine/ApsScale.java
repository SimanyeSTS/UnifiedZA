package com.unifiedza.api.unifiedzabackend.apsengine;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "aps_scale")
public class ApsScale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scale_id", length = 5)
    private Integer scaleId;

    @Column(name = "min_percentage", length = 3, nullable = false)
    private Integer minPercentage;

    @Column(name = "max_percentage", length = 3, nullable = false)
    private Integer maxPercentage;

    @Column(name = "aps_points", length = 2, nullable = false)
    private Integer apsPoints;
}
