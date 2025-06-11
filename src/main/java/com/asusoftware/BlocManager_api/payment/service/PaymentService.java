package com.asusoftware.BlocManager_api.payment.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.payment.model.Payment;
import com.asusoftware.BlocManager_api.payment.model.dto.CreatePaymentDto;
import com.asusoftware.BlocManager_api.payment.model.dto.PaymentDto;
import com.asusoftware.BlocManager_api.payment.repository.PaymentRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper mapper;

    /**
     * Adaugă o nouă plată pentru un apartament.
     */
    public PaymentDto createPayment(CreatePaymentDto dto, Jwt principal) {
        UUID currentUserId = UUID.fromString(principal.getSubject());

        Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                .orElseThrow(() -> new RuntimeException("Apartamentul nu există."));

        // Verifică dacă userul are acces la blocul apartamentului
        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId());

        if (!hasAccess) {
            throw new RuntimeException("Nu aveți acces să adăugați plăți pentru acest apartament.");
        }

        Payment payment = Payment.builder()
                .apartmentId(dto.getApartmentId())
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .note(dto.getNote())
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        return mapper.map(payment, PaymentDto.class);
    }

    /**
     * Returnează toate plățile pentru un apartament.
     */
    public List<PaymentDto> getPaymentsForApartment(UUID apartmentId, Jwt principal) {
        UUID currentUserId = UUID.fromString(principal.getSubject());

        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartamentul nu există."));

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUserId, apartment.getBlockId());

        if (!hasAccess) {
            throw new RuntimeException("Nu aveți acces să vizualizați plățile acestui apartament.");
        }

        return paymentRepository.findAllByApartmentId(apartmentId).stream()
                .map(p -> mapper.map(p, PaymentDto.class))
                .collect(Collectors.toList());
    }

    public PaymentDto getById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plata nu a fost găsită."));
        return mapper.map(payment, PaymentDto.class);
    }
}
