package com.asusoftware.BlocManager_api.user.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(name = "keycloak_id", unique = true, nullable = false)
    private UUID keycloakId;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String stripeAccountId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
