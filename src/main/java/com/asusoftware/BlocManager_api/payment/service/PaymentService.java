package com.asusoftware.BlocManager_api.payment.service;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import com.asusoftware.BlocManager_api.bloc.repository.BlocRepository;
import com.asusoftware.BlocManager_api.payment.model.Payment;
import com.asusoftware.BlocManager_api.payment.model.dto.CreatePaymentDto;
import com.asusoftware.BlocManager_api.payment.model.dto.PaymentDto;
import com.asusoftware.BlocManager_api.payment.repository.PaymentRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import com.asusoftware.BlocManager_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final BlocRepository blocRepository;
    private final UserService userService;
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

    public Page<PaymentDto> getPaymentsForAssociation(UUID associationId, Jwt principal, int page, int size, String search) {

        UUID userId = userService.getUserByKeycloakId(principal);

        boolean hasAccess = userRoleRepository.existsByUserIdAndAssociationId(userId, associationId);
        if (!hasAccess) {
            throw new RuntimeException("Nu aveți acces la această asociație.");
        }

        List<Bloc> blocks = blocRepository.findAllByAssociationId(associationId);
        List<UUID> blockIds = blocks.stream().map(Bloc::getId).toList();
        if (blockIds.isEmpty()) return Page.empty();

        List<Apartment> apartments = apartmentRepository.findAllByBlockIdIn(blockIds);
        List<UUID> apartmentIds = apartments.stream().map(Apartment::getId).toList();
        if (apartmentIds.isEmpty()) return Page.empty();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Payment> payments;
        if (!search.isBlank()) {
            payments = paymentRepository.searchByApartmentIdIn(apartmentIds, search.toLowerCase(), pageable);
        } else {
            payments = paymentRepository.findAllByApartmentIdIn(apartmentIds, pageable);
        }

        return payments.map(payment -> mapper.map(payment, PaymentDto.class));
    }


    public PaymentDto getById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plata nu a fost găsită."));
        return mapper.map(payment, PaymentDto.class);
    }
}
