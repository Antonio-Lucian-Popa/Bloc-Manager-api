package com.asusoftware.BlocManager_api.expense.model.dto;

import com.asusoftware.BlocManager_api.expense.model.ExpenseCategory;
import com.asusoftware.BlocManager_api.expense.model.ExpenseStatus;
import lombok.*;
import java.util.UUID;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseDto {
    private UUID blockId;
    //private String name;
    private String description;
    private Double amount;
    private ExpenseCategory category;
    private LocalDate dueDate;
    private ExpenseStatus status;
}