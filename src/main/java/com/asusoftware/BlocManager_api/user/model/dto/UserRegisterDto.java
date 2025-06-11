package com.asusoftware.BlocManager_api.user.model.dto;

import com.asusoftware.BlocManager_api.user.model.UsersRole;
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
    private UsersRole role;
}