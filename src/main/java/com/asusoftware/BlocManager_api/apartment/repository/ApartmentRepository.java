package com.asusoftware.BlocManager_api.apartment.repository;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID> {
    List<Apartment> findByBlockId(UUID blockId);
    List<Apartment> findAllByBlockId(UUID blockId);

}