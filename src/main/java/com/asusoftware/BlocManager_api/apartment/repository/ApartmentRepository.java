package com.asusoftware.BlocManager_api.apartment.repository;

import com.asusoftware.BlocManager_api.apartment.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID> {
    Optional<Apartment> findByOwnerId(UUID ownerId);
    List<Apartment> findByBlockId(UUID blockId);
    List<Apartment> findAllByBlockId(UUID blockId);
    List<Apartment> findAllByBlockIdIn(List<UUID> blockIds);
    @Query("SELECT b.associationId FROM Bloc b WHERE b.id = :blockId")
    UUID getAssociationIdByBlockId(@Param("blockId") UUID blockId);

}