package com.asusoftware.BlocManager_api.payment.model.dto;


import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentDto {
    private UUID apartmentId;
    private Double amount;
    private String method;
    private String note;
}