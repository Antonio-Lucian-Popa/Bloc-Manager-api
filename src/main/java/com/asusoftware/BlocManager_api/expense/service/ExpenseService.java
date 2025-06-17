package com.asusoftware.BlocManager_api.expense.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.apartment_expense.model.ApartmentExpense;
import com.asusoftware.BlocManager_api.apartment_expense.repository.ApartmentExpenseRepository;
import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import com.asusoftware.BlocManager_api.bloc.repository.BlocRepository;
import com.asusoftware.BlocManager_api.expense.model.Expense;
import com.asusoftware.BlocManager_api.expense.model.ExpenseStatus;
import com.asusoftware.BlocManager_api.expense.model.dto.CreateExpenseDto;
import com.asusoftware.BlocManager_api.expense.model.dto.ExpenseDto;
import com.asusoftware.BlocManager_api.expense.repository.ExpenseRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
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
    private final BlocRepository blockRepository;
    private final ModelMapper mapper;

    /**
     * Creează o cheltuială comună și o distribuie proporțional către apartamente.
     */
    public ExpenseDto create(CreateExpenseDto dto) {
        // Salvează cheltuiala principală
        Expense expense = Expense.builder()
                .blockId(dto.getBlockId())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().withDayOfMonth(1))
                .amount(dto.getAmount())
                .category(dto.getCategory())
                .status(dto.getStatus() != null ? dto.getStatus() : ExpenseStatus.PENDING)
                .createdAt(LocalDate.now().atStartOfDay())
                .build();

        expenseRepository.save(expense);

        // Obține toate apartamentele din bloc
        List<Apartment> apartments = apartmentRepository.findAllByBlockId(dto.getBlockId());

        // Calculează suma per apartament (distribuire egală sau pe bază de suprafață – opțional)
        double amountPerApartment = dto.getAmount() / apartments.size();

        // Salvează înregistrările ApartmentExpense
        for (Apartment apartment : apartments) {
            ApartmentExpense ae = ApartmentExpense.builder()
                    .apartmentId(apartment.getId())
                    .expenseId(expense.getId())
                    .allocatedAmount(amountPerApartment)
                    .month(dto.getDueDate() != null ? dto.getDueDate().getMonthValue() : LocalDate.now().withDayOfMonth(1).getMonthValue())
                    .year(dto.getDueDate() != null ? dto.getDueDate().getYear() : LocalDate.now().getYear())
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

    public Page<ExpenseDto> getByAssociationId(UUID associationId, Pageable pageable, String search) {
        List<Bloc> blocks = blockRepository.findAllByAssociationId(associationId);
        List<UUID> blockIds = blocks.stream()
                .map(Bloc::getId)
                .collect(Collectors.toList());

        if (blockIds.isEmpty()) {
            return Page.empty();
        }

        Page<Expense> page = expenseRepository.findByBlockIdsWithSearch(blockIds, search, pageable);
        return page.map(expense -> mapper.map(expense, ExpenseDto.class));
    }



    public void deleteExpense(UUID expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new RuntimeException("Cheltuiala nu a fost găsită.");
        }
        expenseRepository.deleteById(expenseId);
    }
}
