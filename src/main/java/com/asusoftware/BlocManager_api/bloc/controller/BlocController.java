package com.asusoftware.BlocManager_api.bloc.controller;

import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import com.asusoftware.BlocManager_api.bloc.model.dto.CreateBlocDto;
import com.asusoftware.BlocManager_api.bloc.service.BlocService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blocks")
@RequiredArgsConstructor
public class BlocController {

    private final BlocService blocService;

    @PostMapping("/association/{associationId}")
    public Bloc createBlock(
            @PathVariable UUID associationId,
            @RequestBody CreateBlocDto dto,
            Jwt principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getSubject());
        return blocService.createBlock(associationId, dto, currentUserId);
    }

    @GetMapping("/association/{associationId}")
    public List<Bloc> getBlocksByAssociation(
            @PathVariable UUID associationId,
            Jwt principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getSubject());
        return blocService.getBlocksByAssociation(associationId, currentUserId);
    }
}
