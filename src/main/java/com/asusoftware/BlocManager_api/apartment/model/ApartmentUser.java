package com.asusoftware.BlocManager_api.apartment.model;

import com.asusoftware.BlocManager_api.user.model.UsersRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "apartment_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentUser {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "apartment_id", nullable = false)
    private UUID apartmentId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UsersRole role = UsersRole.TENANT;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
