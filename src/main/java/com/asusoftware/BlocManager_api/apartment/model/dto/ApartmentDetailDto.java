package com.asusoftware.BlocManager_api.apartment.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDetailDto {
    private UUID id;
    private UUID blockId;
    private String number;
    private Integer floor;
    private Double surface;
    private String ownerName;
    private LocalDateTime createdAt;
    private ApartmentUserDto apartmentUser;
}
