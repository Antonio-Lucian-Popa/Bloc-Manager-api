package com.asusoftware.BlocManager_api.expense.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.apartment_expense.model.ApartmentExpense;
import com.asusoftware.BlocManager_api.apartment_expense.repository.ApartmentExpenseRepository;
import com.asusoftware.BlocManager_api.expense.model.Expense;
import com.asusoftware.BlocManager_api.expense.model.dto.CreateExpenseDto;
import com.asusoftware.BlocManager_api.expense.model.dto.ExpenseDto;
import com.asusoftware.BlocManager_api.expense.repository.ExpenseRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ApartmentRepository apartmentRepository;
    private final ApartmentExpenseRepository apartmentExpenseRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper mapper;

    /**
     * Creează o cheltuială comună și o distribuie proporțional către apartamente.
     */
    public ExpenseDto create(CreateExpenseDto dto) {
        // Salvează cheltuiala principală
        Expense expense = Expense.builder()
                .blockId(dto.getBlockId())
                .name(dto.getName())
                .description(dto.getDescription())
                .month(dto.getMonth() != null ? dto.getMonth() : LocalDate.now().withDayOfMonth(1))
                .totalAmount(dto.getTotalAmount())
                .createdAt(LocalDate.now().atStartOfDay())
                .build();

        expenseRepository.save(expense);

        // Obține toate apartamentele din bloc
        List<Apartment> apartments = apartmentRepository.findAllByBlockId(dto.getBlockId());

        // Calculează suma per apartament (distribuire egală sau pe bază de suprafață – opțional)
        double amountPerApartment = dto.getTotalAmount().doubleValue() / apartments.size();

        // Salvează înregistrările ApartmentExpense
        for (Apartment apartment : apartments) {
            ApartmentExpense ae = ApartmentExpense.builder()
                    .apartmentId(apartment.getId())
                    .expenseId(expense.getId())
                    .allocatedAmount(amountPerApartment)
                    .build();
            apartmentExpenseRepository.save(ae);
        }

        return mapper.map(expense, ExpenseDto.class);
    }

    /**
     * Returnează toate cheltuielile pentru un bloc.
     */
    public List<ExpenseDto> getAllForBlock(UUID blockId) {
        return expenseRepository.findAllByBlockId(blockId)
                .stream()
                .map(exp -> mapper.map(exp, ExpenseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Returnează o cheltuială după ID.
     */
    public ExpenseDto getById(UUID id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cheltuiala nu a fost găsită"));
        return mapper.map(expense, ExpenseDto.class);
    }

    public void deleteExpense(UUID expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new RuntimeException("Cheltuiala nu a fost găsită.");
        }
        expenseRepository.deleteById(expenseId);
    }

}
