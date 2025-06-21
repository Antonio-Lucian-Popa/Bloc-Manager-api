package com.asusoftware.BlocManager_api.apartment.model.dto;

import com.asusoftware.BlocManager_api.user.model.UsersRole;
import com.asusoftware.BlocManager_api.user.model.dto.UserDto;
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
public class ApartmentUserDto {
    private UUID apartmentId;
    private UserDto user;
    private UsersRole role;
    private LocalDateTime createdAt;
}
