package com.asusoftware.BlocManager_api.expense.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseDto {
    private UUID blockId;
    private String name;
    private String description;
    private Double totalAmount;
    private LocalDate month;
}