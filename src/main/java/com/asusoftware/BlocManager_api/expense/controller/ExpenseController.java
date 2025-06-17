package com.asusoftware.BlocManager_api.expense.controller;

import com.asusoftware.BlocManager_api.expense.model.dto.CreateExpenseDto;
import com.asusoftware.BlocManager_api.expense.model.dto.ExpenseDto;
import com.asusoftware.BlocManager_api.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Creează o nouă cheltuială pentru un bloc.
     */
    @PostMapping
    public ExpenseDto createExpense(@Valid @RequestBody CreateExpenseDto dto) {
        return expenseService.create(dto);
    }

    /**
     * Returnează toate cheltuielile aferente unui bloc.
     */
    @GetMapping("/block/{blockId}")
    public List<ExpenseDto> getExpensesByBlock(@PathVariable UUID blockId) {
        return expenseService.getAllForBlock(blockId);
    }

    /**
     * Returnează detaliile unei cheltuieli specifice.
     */
    @GetMapping("/{expenseId}")
    public ExpenseDto getExpenseById(@PathVariable UUID expenseId) {
        return expenseService.getById(expenseId);
    }

    @GetMapping("/by-association/{associationId}")
    public Page<ExpenseDto> getByAssociation(
            @PathVariable UUID associationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return expenseService.getByAssociationId(associationId, pageable, search);
    }


    /**
     * Șterge o cheltuială după ID.
     */
    @DeleteMapping("/{expenseId}")
    public void deleteExpense(@PathVariable UUID expenseId) {
        expenseService.deleteExpense(expenseId);
    }
}
