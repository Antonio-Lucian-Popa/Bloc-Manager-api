package com.asusoftware.BlocManager_api.user.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRoleDto {
    private UUID userId;
    private UUID associationId;
    private UUID blockId;
    private String role;
}
