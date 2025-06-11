package com.asusoftware.BlocManager_api.apartment.controller;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.model.dto.CreateApartmentDto;
import com.asusoftware.BlocManager_api.apartment.service.ApartmentService;
import lombok.RequiredArgsConstructor;
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
            Jwt principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getSubject());
        return apartmentService.createApartment(blockId, dto, currentUserId);
    }

    @GetMapping("/block/{blockId}")
    public List<Apartment> getApartmentsInBlock(
            @PathVariable UUID blockId,
            Jwt principal
    ) {
        UUID currentUserId = UUID.fromString(principal.getSubject());
        return apartmentService.getApartmentsInBlock(blockId, currentUserId);
    }
}
