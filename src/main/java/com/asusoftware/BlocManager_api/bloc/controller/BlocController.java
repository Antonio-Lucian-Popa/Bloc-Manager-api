package com.asusoftware.BlocManager_api.bloc.controller;

import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import com.asusoftware.BlocManager_api.bloc.model.dto.CreateBlocDto;
import com.asusoftware.BlocManager_api.bloc.service.BlocService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal Jwt principal
    ) {
        return blocService.createBlock(associationId, dto, principal);
    }

    @GetMapping("/association/{associationId}")
    public List<Bloc> getBlocksByAssociation(
            @PathVariable UUID associationId,
            @AuthenticationPrincipal Jwt principal
    ) {
        return blocService.getBlocksByAssociation(associationId, principal);
    }
}
