package com.asusoftware.BlocManager_api.payment.controller;

import com.asusoftware.BlocManager_api.payment.model.dto.CreatePaymentDto;
import com.asusoftware.BlocManager_api.payment.model.dto.PaymentDto;
import com.asusoftware.BlocManager_api.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody @Valid CreatePaymentDto dto, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(paymentService.createPayment(dto, jwt));
    }

    @GetMapping("/apartment/{apartmentId}")
    public ResponseEntity<List<PaymentDto>> getForApartment(@PathVariable UUID apartmentId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(paymentService.getPaymentsForApartment(apartmentId, jwt));
    }

    @GetMapping("/association/{associationId}")
    public ResponseEntity<Page<PaymentDto>> getForAssociation(
            @PathVariable UUID associationId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<PaymentDto> payments = paymentService.getPaymentsForAssociation(associationId, jwt, page, size, search);
        return ResponseEntity.ok(payments);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }
}