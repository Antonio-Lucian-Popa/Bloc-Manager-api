package com.asusoftware.BlocManager_api.payment.repository;

import com.asusoftware.BlocManager_api.payment.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByApartmentId(UUID apartmentId);
    List<Payment> findAllByApartmentId(UUID apartmentId);
    List<Payment> findAllByApartmentIdIn(List<UUID> apartmentIds);

    Page<Payment> findAllByApartmentIdIn(List<UUID> apartmentIds, Pageable pageable);

    @Query("""
    SELECT p FROM Payment p
    WHERE p.apartmentId IN :apartmentIds
      AND (
          LOWER(CAST(p.note AS string)) LIKE LOWER(CONCAT('%', :search, '%'))
          OR CAST(p.amount AS string) LIKE CONCAT('%', :search, '%')
      )
""")
    Page<Payment> searchByApartmentIdIn(
            @Param("apartmentIds") List<UUID> apartmentIds,
            @Param("search") String search,
            Pageable pageable
    );

}
