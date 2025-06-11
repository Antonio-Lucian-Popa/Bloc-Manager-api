package com.asusoftware.BlocManager_api.user.service;

import com.asusoftware.BlocManager_api.config.KeycloakService;
import com.asusoftware.BlocManager_api.user.model.User;
import com.asusoftware.BlocManager_api.user.model.UserRole;
import com.asusoftware.BlocManager_api.user.model.dto.LoginDto;
import com.asusoftware.BlocManager_api.user.model.dto.LoginResponseDto;
import com.asusoftware.BlocManager_api.user.model.dto.UserRegisterDto;
import com.asusoftware.BlocManager_api.user.repository.UserRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * Înregistrează un nou utilizator în Keycloak și în baza de date locală.
     */
    @Transactional
    public User registerUser(UserRegisterDto dto) {
        // Creează userul în Keycloak și obține ID-ul acestuia
        String keycloakId = keycloakService.createKeycloakUser(dto);

        // Creează local userul
        User user = User.builder()
                .id(UUID.randomUUID())
                .keycloakId(UUID.fromString(keycloakId))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .build();
        userRepository.save(user);

        // Creează local rolul (ex: ADMIN_ASSOCIATION) – pentru început fără asociere la bloc/asociație
        UserRole role = UserRole.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .role(dto.getRole())
                .build();
        userRoleRepository.save(role);

        return user;
    }

    public LoginResponseDto login(LoginDto dto) {
        return keycloakService.loginUser(dto);
    }

    public LoginResponseDto refresh(String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }

}