package com.asusoftware.BlocManager_api.user.repository;

import com.asusoftware.BlocManager_api.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findByUserId(UUID userId);
    List<UserRole> findByAssociationId(UUID associationId);
    List<UserRole> findByBlockId(UUID blockId);
}
