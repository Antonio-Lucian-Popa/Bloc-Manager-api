package com.asusoftware.BlocManager_api.association.controller;

import com.asusoftware.BlocManager_api.association.model.dto.AssociationDto;
import com.asusoftware.BlocManager_api.association.model.dto.CreateAssociationDto;
import com.asusoftware.BlocManager_api.association.model.dto.InviteUserDto;
import com.asusoftware.BlocManager_api.association.service.AssociationService;
import com.asusoftware.BlocManager_api.user.model.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/associations")
@RequiredArgsConstructor
public class AssociationController {

    private final AssociationService associationService;

    /**
     * Creează o nouă asociație (doar pentru utilizatori cu rol ADMIN_ASSOCIATION).
     */
    @PostMapping
    public AssociationDto createAssociation(@Valid @RequestBody CreateAssociationDto dto, @AuthenticationPrincipal Jwt principal) {
        return associationService.createAssociation(dto, principal);
    }

    /**
     * Listează toate asociațiile create de userul curent.
     */
    @GetMapping("/my")
    public Page<AssociationDto> getMyAssociations(
            @AuthenticationPrincipal Jwt principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        return associationService.getMyAssociations(principal, page, size, search);
    }


    /**
     * Invita un user existent într-o asociație cu un anumit rol.
     */
    @PostMapping("/{associationId}/invite")
    public void inviteUserToAssociation(
            @PathVariable UUID associationId,
            @Valid @RequestBody InviteUserDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        associationService.inviteUserToAssociation(associationId, dto.getEmail(), dto.getRole(), dto.getBlocId(), principal);
    }


    @GetMapping("/users/{associationId}")
    public ResponseEntity<Page<UserDto>> getUsersByAssociation(
            @PathVariable UUID associationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @AuthenticationPrincipal Jwt principal
    ) {
        return ResponseEntity.ok(associationService.getUsersByAssociation(associationId, page, size, search, principal));
    }
}
