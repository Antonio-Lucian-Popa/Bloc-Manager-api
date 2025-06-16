package com.asusoftware.BlocManager_api.association.controller;

import com.asusoftware.BlocManager_api.association.model.dto.AssociationDto;
import com.asusoftware.BlocManager_api.association.model.dto.CreateAssociationDto;
import com.asusoftware.BlocManager_api.association.model.dto.InviteUserDto;
import com.asusoftware.BlocManager_api.association.service.AssociationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<AssociationDto> getMyAssociations(@AuthenticationPrincipal Jwt principal) {
        return associationService.getMyAssociations(principal);
    }

    /**
     * Invita un user existent într-o asociație cu un anumit rol.
     */
    @PostMapping("/{associationId}/invite")
    public void inviteUserToAssociation(
            @PathVariable UUID associationId,
            @Valid @RequestBody InviteUserDto dto,
            @RequestAttribute Jwt principal
    ) {
        associationService.inviteUserToAssociation(associationId, dto.getUserId(), dto.getRole(), principal);
    }
}
