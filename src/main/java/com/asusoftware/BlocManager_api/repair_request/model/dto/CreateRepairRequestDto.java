package com.asusoftware.BlocManager_api.repair_request.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRepairRequestDto {
    private UUID apartmentId;
    private UUID blockId;
    private UUID submittedBy;
    private String description;
}