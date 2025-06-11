package com.asusoftware.BlocManager_api.meter_reading.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.meter_reading.model.MeterReading;
import com.asusoftware.BlocManager_api.meter_reading.model.dto.CreateMeterReadingDto;
import com.asusoftware.BlocManager_api.meter_reading.model.dto.MeterReadingDto;
import com.asusoftware.BlocManager_api.meter_reading.repository.MeterReadingRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import com.asusoftware.BlocManager_api.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    /**
     * Adaugă o nouă citire pentru un apartament.
     */
    @Transactional
    public MeterReadingDto createMeterReading(CreateMeterReadingDto dto, Jwt jwt) {
        UUID currentUserId = userService.getUserByKeycloakId(jwt);

        Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                .orElseThrow(() -> new RuntimeException("Apartamentul nu a fost găsit."));

        // Verificare acces: user-ul trebuie să aibă acces la blocul sau asociația apartamentului
        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId());

        if (!hasAccess) {
            throw new RuntimeException("Nu ai permisiunea de a adăuga citiri pentru acest apartament.");
        }

        MeterReading reading = MeterReading.builder()
                .apartmentId(dto.getApartmentId())
                .type(dto.getType())
                .value(dto.getValue())
                .readingDate(dto.getReadingDate())
                .photoUrl(dto.getPhotoUrl())
                .build();

        meterReadingRepository.save(reading);
        return mapper.map(reading, MeterReadingDto.class);
    }

    /**
     * Listează toate citirile pentru un apartament.
     */
    public List<MeterReadingDto> getReadingsForApartment(UUID apartmentId, Jwt jwt) {
        UUID currentUserId = userService.getUserByKeycloakId(jwt);

        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartamentul nu a fost găsit."));

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId());

        if (!hasAccess) {
            throw new RuntimeException("Nu ai permisiunea de a vizualiza citirile acestui apartament.");
        }

        return meterReadingRepository.findAllByApartmentId(apartmentId).stream()
                .map(r -> mapper.map(r, MeterReadingDto.class))
                .collect(Collectors.toList());
    }
}