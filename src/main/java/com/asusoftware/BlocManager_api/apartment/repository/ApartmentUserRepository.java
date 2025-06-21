package com.asusoftware.BlocManager_api.apartment.repository;

import com.asusoftware.BlocManager_api.apartment.model.ApartmentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApartmentUserRepository extends JpaRepository<ApartmentUser, UUID> {
    Optional<ApartmentUser> findByApartmentId(UUID id);
}
