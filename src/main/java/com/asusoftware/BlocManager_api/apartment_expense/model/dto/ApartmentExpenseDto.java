package com.asusoftware.BlocManager_api.apartment_expense.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentExpenseDto {
    private UUID id;
    private UUID apartmentId;
    private UUID expenseId;
    private Double allocatedAmount;
}
