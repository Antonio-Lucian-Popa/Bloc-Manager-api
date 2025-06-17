package com.asusoftware.BlocManager_api.user.repository;

import com.asusoftware.BlocManager_api.user.model.User;
import com.asusoftware.BlocManager_api.user.model.UsersRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByKeycloakId(UUID keycloakId);
    Optional<User> findByStripeAccountId(String stripeAccountId);

    Page<User> findAllByIdIn(Collection<UUID> ids, Pageable pageable);

//    List<User> findByRole(UsersRole role, Pageable pageable);

    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN UserRole ur ON ur.userId = u.id
    WHERE ur.associationId = :associationId
    AND (
        LOWER(u.firstName) LIKE %:search% OR
        LOWER(u.lastName) LIKE %:search% OR
        LOWER(u.email) LIKE %:search%
    )
""")
    Page<User> findDistinctByIdInAndSearch(
            @Param("associationId") UUID associationId,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN UserRole ur ON ur.userId = u.id
    WHERE ur.associationId = :associationId
""")
    Page<User> findDistinctByIdIn(
            @Param("associationId") UUID associationId,
            Pageable pageable
    );
}
