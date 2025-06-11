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

    private String name;

    private String description;

    @Column(name = "total_amount")
    private Double totalAmount;

    private LocalDate month;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}