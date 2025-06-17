package com.asusoftware.BlocManager_api.expense.model.dto;

import com.asusoftware.BlocManager_api.bloc.model.dto.BlocDto;
import com.asusoftware.BlocManager_api.expense.model.ExpenseCategory;
import com.asusoftware.BlocManager_api.expense.model.ExpenseStatus;
import lombok.*;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private UUID id;
    private BlocDto bloc;
    private String description;
    private Double amount;
    private ExpenseCategory category;
    private LocalDate dueDate;
    private ExpenseStatus status;
    private LocalDateTime createdAt;
}
