package com.asusoftware.BlocManager_api.payment.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "apartment_id", nullable = false)
    private UUID apartmentId;

    private Double amount;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    private String method;

    private String note;
}
