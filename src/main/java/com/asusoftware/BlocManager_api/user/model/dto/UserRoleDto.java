package com.asusoftware.BlocManager_api.user.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {
    private UUID id;
    private UUID userId;
    private UUID associationId;
    private UUID blockId;
    private String role;
    private LocalDateTime createdAt;
}