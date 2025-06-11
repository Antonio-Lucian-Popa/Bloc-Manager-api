package com.asusoftware.BlocManager_api.repair_request.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequestDto {
    private UUID id;
    private UUID apartmentId;
    private UUID blockId;
    private UUID submittedBy;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
