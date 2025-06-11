package com.asusoftware.BlocManager_api.meter_reading.controller;

import com.asusoftware.BlocManager_api.meter_reading.model.dto.CreateMeterReadingDto;
import com.asusoftware.BlocManager_api.meter_reading.model.dto.MeterReadingDto;
import com.asusoftware.BlocManager_api.meter_reading.service.MeterReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meter-readings")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    /**
     * Adaugă o nouă citire pentru un apartament.
     */
    @PostMapping
    public ResponseEntity<MeterReadingDto> createReading(
            @RequestBody CreateMeterReadingDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        MeterReadingDto created = meterReadingService.createMeterReading(dto, principal);
        return ResponseEntity.ok(created);
    }

    /**
     * Returnează toate citirile pentru un apartament.
     */
    @GetMapping("/apartment/{apartmentId}")
    public ResponseEntity<List<MeterReadingDto>> getAllForApartment(
            @PathVariable UUID apartmentId,
            @AuthenticationPrincipal Jwt principal
    ) {
        List<MeterReadingDto> readings = meterReadingService.getReadingsForApartment(apartmentId, principal);
        return ResponseEntity.ok(readings);
    }
}