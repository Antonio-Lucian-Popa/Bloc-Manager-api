package com.asusoftware.BlocManager_api.apartment.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDto {
    private UUID id;
    private UUID blockId;
    private String number;
    private Integer floor;
    private Double surface;
    private String ownerName;
    private LocalDateTime createdAt;
}
