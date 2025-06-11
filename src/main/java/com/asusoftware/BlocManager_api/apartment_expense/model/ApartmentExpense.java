package com.asusoftware.BlocManager_api.apartment_expense.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "apartment_expenses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentExpense {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "apartment_id", nullable = false)
    private UUID apartmentId;

    @Column(name = "expense_id", nullable = false)
    private UUID expenseId;

    @Column(name = "allocated_amount")
    private Double allocatedAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;
}
