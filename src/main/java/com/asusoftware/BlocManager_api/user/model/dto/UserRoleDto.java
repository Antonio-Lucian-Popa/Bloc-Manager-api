package com.asusoftware.BlocManager_api.user.model.dto;

import com.asusoftware.BlocManager_api.user.model.UsersRole;
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
    private UsersRole role;
    private LocalDateTime createdAt;
}