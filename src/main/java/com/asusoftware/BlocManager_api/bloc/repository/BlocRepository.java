package com.asusoftware.BlocManager_api.bloc.repository;

import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlocRepository extends JpaRepository<Bloc, UUID> {
    List<Bloc> findByAssociationId(UUID associationId);
    List<Bloc> findAllByAssociationId(UUID associationId);
    @Query("SELECT b.associationId FROM Bloc b WHERE b.id = :blockId")
    UUID findAssociationIdByBlockId(@Param("blockId") UUID blockId);

    Page<Bloc> findByAssociationId(UUID associationId, Pageable pageable);

    Page<Bloc> findByAssociationIdAndNameContainingIgnoreCase(UUID associationId, String name, Pageable pageable);

}
