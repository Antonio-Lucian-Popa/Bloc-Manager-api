package com.asusoftware.BlocManager_api.association.model.dto;

import com.asusoftware.BlocManager_api.user.model.UsersRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InviteUserDto {
    @NotNull
    private UUID userId;

    @NotNull
    private UsersRole role;
}
