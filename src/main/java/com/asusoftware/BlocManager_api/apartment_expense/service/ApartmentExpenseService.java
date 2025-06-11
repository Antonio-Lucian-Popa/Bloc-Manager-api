package com.asusoftware.BlocManager_api.apartment_expense.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.apartment_expense.model.ApartmentExpense;
import com.asusoftware.BlocManager_api.apartment_expense.model.dto.ApartmentExpenseDto;
import com.asusoftware.BlocManager_api.apartment_expense.model.dto.CreateApartmentExpenseDto;
import com.asusoftware.BlocManager_api.apartment_expense.repository.ApartmentExpenseRepository;
import com.asusoftware.BlocManager_api.bloc.repository.BlocRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import com.asusoftware.BlocManager_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentExpenseService {

    private final ApartmentExpenseRepository apartmentExpenseRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final BlocRepository blockRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    /**
     * Creează o cheltuială pentru un apartament.
     */
    @Transactional
    public ApartmentExpenseDto createExpense(CreateApartmentExpenseDto dto, Jwt principal) {
        UUID currentUserId = UUID.fromString(principal.getSubject());

        Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                .orElseThrow(() -> new RuntimeException("Apartament inexistent"));

        // Verificare dacă userul are acces la blocul apartamentului
        UUID blockId = apartment.getBlockId();
        UUID associationId = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Blocul nu a fost găsit"))
                .getAssociationId();

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, blockId)
                || userRoleRepository.existsByUserIdAndAssociationId(currentUserId, associationId);

        if (!hasAccess) {
            throw new RuntimeException("Nu ai permisiunea de a adăuga cheltuieli pentru acest apartament.");
        }

        ApartmentExpense expense = ApartmentExpense.builder()
                .apartmentId(dto.getApartmentId())
                .allocatedAmount(dto.getAllocatedAmount())
                .description(dto.getDescription())
                .month(dto.getMonth())
                .year(dto.getYear())
                .build();

        apartmentExpenseRepository.save(expense);
        return mapper.map(expense, ApartmentExpenseDto.class);
    }

    /**
     * Returnează toate cheltuielile pentru un apartament.
     */
    public List<ApartmentExpenseDto> getExpensesForApartment(UUID apartmentId, Jwt principal) {
        UUID currentUserId = UUID.fromString(principal.getSubject());

        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartament inexistent"));

        UUID blockId = apartment.getBlockId();
        UUID associationId = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Blocul nu a fost găsit"))
                .getAssociationId();

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId())
                || userRoleRepository.existsByUserIdAndAssociationId(currentUserId, associationId);

        if (!hasAccess) {
            throw new RuntimeException("Nu ai acces la acest apartament.");
        }

        return apartmentExpenseRepository.findAllByApartmentId(apartmentId).stream()
                .map(e -> mapper.map(e, ApartmentExpenseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Returnează toate cheltuielile pentru un anumit apartament
     */
    public List<ApartmentExpenseDto> getByApartment(UUID apartmentId, Jwt principal) {
        UUID currentUserId = userService.getUserByKeycloakId(principal);

        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartament inexistent."));

        UUID blockId = apartment.getBlockId();
        UUID associationId = apartmentRepository.getAssociationIdByApartmentId(apartmentId);

        boolean hasAccess =
                userRoleRepository.existsByUserIdAndBlockId(currentUserId, blockId) ||
                        userRoleRepository.existsByUserIdAndAssociationId(currentUserId, associationId);

        if (!hasAccess) {
            throw new RuntimeException("Nu ai acces la acest apartament.");
        }

        return apartmentExpenseRepository.findAllByApartmentId(apartmentId).stream()
                .map(entity -> mapper.map(entity, ApartmentExpenseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Returnează toate apartamentele care au primit o cheltuială specifică
     */
    public List<ApartmentExpenseDto> getByExpense(UUID expenseId) {
        return apartmentExpenseRepository.findAllByExpenseId(expenseId).stream()
                .map(entity -> mapper.map(entity, ApartmentExpenseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Șterge o cheltuială (doar dacă userul are acces la apartament).
     */
    @Transactional
    public void deleteExpense(UUID expenseId, Jwt principal) {
        UUID currentUserId = UUID.fromString(principal.getSubject());

        ApartmentExpense expense = apartmentExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Cheltuială inexistentă"));

        Apartment apartment = apartmentRepository.findById(expense.getApartmentId())
                .orElseThrow(() -> new RuntimeException("Apartament inexistent"));

        UUID blockId = apartment.getBlockId();
        UUID associationId = blockRepository.findById(blockId)
                .orElseThrow(() -> new RuntimeException("Blocul nu a fost găsit"))
                .getAssociationId();

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId())
                || userRoleRepository.existsByUserIdAndAssociationId(currentUserId, associationId);

        if (!hasAccess) {
            throw new RuntimeException("Nu ai dreptul să ștergi această cheltuială.");
        }

        apartmentExpenseRepository.deleteById(expenseId);
    }
}