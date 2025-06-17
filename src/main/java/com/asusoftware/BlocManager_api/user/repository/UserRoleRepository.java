package com.asusoftware.BlocManager_api.user.repository;

import com.asusoftware.BlocManager_api.user.model.UserRole;
import com.asusoftware.BlocManager_api.user.model.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    Optional<UserRole> findByUserId(UUID userId);
   // List<UserRole> findByUserId(UUID userId);
    List<UserRole> findByAssociationId(UUID associationId);
    List<UserRole> findByBlockId(UUID blockId);
    boolean existsByUserIdAndRole(UUID userId, UsersRole role);
    boolean existsByUserIdAndAssociationIdAndRole(UUID userId, UUID associationId, UsersRole role);
    boolean existsByUserIdAndAssociationId(UUID userId, UUID associationId);
    boolean existsByUserIdAndBlockId(UUID userId, UUID blockId);

    List<UserRole> findAllByRole(UsersRole role);

    // Sau dacă vrei doar userIds:
    @Query("SELECT ur.userId FROM UserRole ur WHERE ur.role = :role")
    List<UUID> findUserIdsByRole(@Param("role") UsersRole role);

    List<UserRole> findByAssociationIdAndUserIdIn(UUID associationId, List<UUID> userIds);

}
