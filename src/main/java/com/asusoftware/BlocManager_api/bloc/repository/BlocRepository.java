package com.asusoftware.BlocManager_api.bloc.repository;

import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlocRepository extends JpaRepository<Bloc, UUID> {
    List<Bloc> findByAssociationId(UUID associationId);
}
