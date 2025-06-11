package com.asusoftware.BlocManager_api.user.controller;

import com.asusoftware.BlocManager_api.user.model.dto.LoginDto;
import com.asusoftware.BlocManager_api.user.model.dto.LoginResponseDto;
import com.asusoftware.BlocManager_api.user.model.dto.UserDto;
import com.asusoftware.BlocManager_api.user.model.dto.UserRegisterDto;
import com.asusoftware.BlocManager_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Înregistrare utilizator (creează în Keycloak + local DB).
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterDto dto) {
        var user = userService.registerUser(dto);
        return ResponseEntity.ok(user);
    }

    /**
     * Login utilizator (returnează token + refresh token).
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    /**
     * Reîmprospătare token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(userService.refresh(refreshToken));
    }

    /**
     * Obține datele utilizatorului curent logat.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        return ResponseEntity.ok(userService.getCurrentUser(principal));
    }
}
