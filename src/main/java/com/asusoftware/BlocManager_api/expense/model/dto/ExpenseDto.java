package com.asusoftware.BlocManager_api.expense.model.dto;

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
    private UUID blockId;
    private String name;
    private String description;
    private Double totalAmount;
    private LocalDate month;
    private LocalDateTime createdAt;
}
