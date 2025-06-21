package com.asusoftware.BlocManager_api.apartment.controller;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.model.dto.ApartmentDetailDto;
import com.asusoftware.BlocManager_api.apartment.model.dto.ApartmentDto;
import com.asusoftware.BlocManager_api.apartment.model.dto.CreateApartmentDto;
import com.asusoftware.BlocManager_api.apartment.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @PostMapping("/block/{blockId}")
    public Apartment createApartment(
            @PathVariable UUID blockId,
            @RequestBody CreateApartmentDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getSubject());
        return apartmentService.createApartment(blockId, dto, currentUserId);
    }

    @GetMapping("/block/{blockId}")
    public List<Apartment> getApartmentsInBlock(
            @PathVariable UUID blockId,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getSubject());
        return apartmentService.getApartmentsInBlock(blockId, currentUserId);
    }

    @GetMapping("/my")
    public ResponseEntity<ApartmentDetailDto> getMyApartment(
            @AuthenticationPrincipal Jwt principal
    ) {
        return ResponseEntity.ok(apartmentService.getMyApartment(principal));
    }
}
