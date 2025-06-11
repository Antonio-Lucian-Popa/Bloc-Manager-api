package com.asusoftware.BlocManager_api.apartment_expense.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApartmentExpenseDto {
    private UUID apartmentId;
    private UUID expenseId;
    private Double allocatedAmount;
    private String description;
    private Integer month;
    private Integer year;
}
