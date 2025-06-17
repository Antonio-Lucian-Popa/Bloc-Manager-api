package com.asusoftware.BlocManager_api.bloc.controller;

import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import com.asusoftware.BlocManager_api.bloc.model.dto.BlocDto;
import com.asusoftware.BlocManager_api.bloc.model.dto.CreateBlocDto;
import com.asusoftware.BlocManager_api.bloc.service.BlocService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    public Page<BlocDto> getBlocksByAssociation(
            @PathVariable UUID associationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            @AuthenticationPrincipal Jwt principal
    ) {
        return blocService.getBlocksByAssociation(associationId, page, size, search, principal);
    }

    @GetMapping("/associationList/{associationId}")
    public ResponseEntity<List<BlocDto>> getBlocksByAssociation(
            @PathVariable UUID associationId,
            @AuthenticationPrincipal Jwt principal
    ) {
        return ResponseEntity.ok(blocService.getBlocksByAssociation(associationId, principal));
    }

}
