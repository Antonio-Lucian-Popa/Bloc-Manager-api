package com.asusoftware.BlocManager_api.apartment_expense.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.apartment_expense.model.ApartmentExpense;
import com.asusoftware.BlocManager_api.apartment_expense.model.dto.ApartmentExpenseDto;
import com.asusoftware.BlocManager_api.apartment_expense.model.dto.CreateApartmentExpenseDto;
import com.asusoftware.BlocManager_api.apartment_expense.repository.ApartmentExpenseRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
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
        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId())
                || userRoleRepository.existsByUserIdAndAssociationId(currentUserId, apartment.getAssociationId());

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

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId())
                || userRoleRepository.existsByUserIdAndAssociationId(currentUserId, apartment.getAssociationId());

        if (!hasAccess) {
            throw new RuntimeException("Nu ai acces la acest apartament.");
        }

        return apartmentExpenseRepository.findAllByApartmentId(apartmentId).stream()
                .map(e -> mapper.map(e, ApartmentExpenseDto.class))
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

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId())
                || userRoleRepository.existsByUserIdAndAssociationId(currentUserId, apartment.getAssociationId());

        if (!hasAccess) {
            throw new RuntimeException("Nu ai dreptul să ștergi această cheltuială.");
        }

        apartmentExpenseRepository.deleteById(expenseId);
    }
}