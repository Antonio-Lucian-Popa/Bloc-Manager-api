package com.asusoftware.BlocManager_api.payment.repository;

import com.asusoftware.BlocManager_api.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByApartmentId(UUID apartmentId);
    List<Payment> findAllByApartmentId(UUID apartmentId);
}
