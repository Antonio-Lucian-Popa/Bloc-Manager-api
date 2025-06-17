package com.asusoftware.BlocManager_api.expense.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "block_id", nullable = false)
    private UUID blockId;

    private String description;

    @Column(name = "total_amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ExpenseCategory category;

    private LocalDate dueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}