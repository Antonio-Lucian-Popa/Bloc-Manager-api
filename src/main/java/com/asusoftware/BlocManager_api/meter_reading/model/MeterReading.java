package com.asusoftware.BlocManager_api.meter_reading.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "meter_readings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterReading {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "apartment_id", nullable = false)
    private UUID apartmentId;

    private String type;

    private Double value;

    @Column(name = "reading_date")
    private LocalDate readingDate;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
