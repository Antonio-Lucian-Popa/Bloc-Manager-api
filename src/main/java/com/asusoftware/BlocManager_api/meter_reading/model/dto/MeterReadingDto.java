package com.asusoftware.BlocManager_api.meter_reading.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterReadingDto {
    private UUID id;
    private UUID apartmentId;
    private String type;
    private Double value;
    private LocalDate readingDate;
    private String photoUrl;
    private LocalDateTime createdAt;
}