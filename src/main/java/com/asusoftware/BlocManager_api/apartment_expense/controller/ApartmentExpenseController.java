package com.asusoftware.BlocManager_api.apartment_expense.controller;

import com.asusoftware.BlocManager_api.apartment_expense.model.dto.ApartmentExpenseDto;
import com.asusoftware.BlocManager_api.apartment_expense.model.dto.CreateApartmentExpenseDto;
import com.asusoftware.BlocManager_api.apartment_expense.service.ApartmentExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/apartment-expenses")
@RequiredArgsConstructor
public class ApartmentExpenseController {

    private final ApartmentExpenseService apartmentExpenseService;

    /**
     * Creează o cheltuială pentru un apartament
     */
    @PostMapping
    public ResponseEntity<ApartmentExpenseDto> createExpense(
            @RequestBody CreateApartmentExpenseDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        ApartmentExpenseDto result = apartmentExpenseService.createExpense(dto, principal);
        return ResponseEntity.ok(result);
    }

    /**
     * Returnează toate cheltuielile pentru un anumit apartament
     */
    @GetMapping("/by-apartment/{apartmentId}")
    public ResponseEntity<List<ApartmentExpenseDto>> getByApartment(
            @PathVariable UUID apartmentId,
            @AuthenticationPrincipal Jwt principal
    ) {
        List<ApartmentExpenseDto> result = apartmentExpenseService.getByApartment(apartmentId, principal);
        return ResponseEntity.ok(result);
    }

    /**
     * Returnează toate cheltuielile alocate unei cheltuieli comune
     */
    @GetMapping("/by-expense/{expenseId}")
    public ResponseEntity<List<ApartmentExpenseDto>> getByExpense(@PathVariable UUID expenseId) {
        List<ApartmentExpenseDto> result = apartmentExpenseService.getByExpense(expenseId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        apartmentExpenseService.deleteExpense(id, principal);
        return ResponseEntity.noContent().build();
    }

}
