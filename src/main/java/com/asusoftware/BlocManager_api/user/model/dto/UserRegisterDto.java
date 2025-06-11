package com.asusoftware.BlocManager_api.user.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}