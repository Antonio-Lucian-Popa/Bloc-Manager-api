package com.asusoftware.BlocManager_api.user.service;

import com.asusoftware.BlocManager_api.config.KeycloakService;
import com.asusoftware.BlocManager_api.user.model.User;
import com.asusoftware.BlocManager_api.user.model.UserRole;
import com.asusoftware.BlocManager_api.user.model.dto.*;
import com.asusoftware.BlocManager_api.user.repository.UserRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakService keycloakService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper mapper;

    /**
     * Înregistrează un nou utilizator în Keycloak și în baza de date locală.
     */
    @Transactional
    public UserDto registerUser(UserRegisterDto dto) {
        // Creează userul în Keycloak și obține ID-ul acestuia
        String keycloakId = keycloakService.createKeycloakUser(dto);

        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Rolul utilizatorului este obligatoriu.");
        }

        // Creează local userul
        User user = User.builder()
                .keycloakId(UUID.fromString(keycloakId))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .build();
        userRepository.save(user);

        // Creează local rolul (ex: ADMIN_ASSOCIATION) – pentru început fără asociere la bloc/asociație
        UserRole role = UserRole.builder()
                .userId(user.getId())
                .role(dto.getRole())
                .build();
        userRoleRepository.save(role);

        return mapper.map(user, UserDto.class);
    }

    public LoginResponseDto login(LoginDto dto) {
        return keycloakService.loginUser(dto);
    }

    public LoginResponseDto refresh(String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }

    public UserDto getCurrentUser(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = mapper.map(user, UserDto.class);
        // Obține rolul avansat
        userRoleRepository.findByUserId(user.getId()).ifPresent(role -> {
            userDto.setRole(mapper.map(role, UserRoleDto.class));
        });
        return userDto;
    }

    public User getCurrentUserEntity(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UUID getUserByKeycloakId(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }


}