package com.asusoftware.BlocManager_api.payment.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private UUID id;
    private UUID apartmentId;
    private Double amount;
    private LocalDateTime paidAt;
    private String method;
    private String note;
}
