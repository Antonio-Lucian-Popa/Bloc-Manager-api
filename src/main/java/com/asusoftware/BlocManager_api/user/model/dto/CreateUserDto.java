package com.asusoftware.BlocManager_api.user.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
}
