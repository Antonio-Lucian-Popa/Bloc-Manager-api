package com.asusoftware.BlocManager_api.association.repository;

import com.asusoftware.BlocManager_api.association.model.Association;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssociationRepository extends JpaRepository<Association, UUID> {
    List<Association> findAllByCreatedBy(UUID createdBy);

    Page<Association> findByCreatedBy(UUID createdBy, Pageable pageable);

    Page<Association> findByCreatedByAndNameContainingIgnoreCase(UUID createdBy, String name, Pageable pageable);

}
