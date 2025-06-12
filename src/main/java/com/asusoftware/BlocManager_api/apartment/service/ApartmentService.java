package com.asusoftware.BlocManager_api.apartment.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.model.dto.CreateApartmentDto;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.bloc.repository.BlocRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final BlocRepository blockRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * Creează un apartament într-un bloc (doar de către ADMIN_ASSOCIATION sau BLOCK_ADMIN).
     */
    @Transactional
    public Apartment createApartment(UUID blockId, CreateApartmentDto dto, UUID currentUserId) {
        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, blockId) ||
                userRoleRepository.existsByUserIdAndAssociationId(
                        currentUserId,
                        blockRepository.findAssociationIdByBlockId(blockId)
                );

        if (!hasAccess) {
            throw new RuntimeException("Nu aveți permisiune pentru a adăuga apartamente.");
        }

        Apartment apartment = Apartment.builder()
                .blockId(blockId)
                .number(dto.getNumber())
                .ownerName(dto.getOwnerName())
                .floor(dto.getFloor())
                .build();

        return apartmentRepository.save(apartment);
    }

    public List<Apartment> getApartmentsInBlock(UUID blockId, UUID currentUserId) {
        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, blockId);
        if (!hasAccess) {
            throw new RuntimeException("Acces interzis la acest bloc.");
        }

        return apartmentRepository.findAllByBlockId(blockId);
    }
}

