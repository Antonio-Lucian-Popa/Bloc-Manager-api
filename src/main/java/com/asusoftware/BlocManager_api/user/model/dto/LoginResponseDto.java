package com.asusoftware.BlocManager_api.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    public String accessToken;
    public String refreshToken;
    public long expiresIn;
}
