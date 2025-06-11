package com.asusoftware.BlocManager_api.apartment.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApartmentDto {
    private UUID blockId;
    private String number;
    private Integer floor;
    private Double surface;
    private String ownerName;
}
