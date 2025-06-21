package com.asusoftware.BlocManager_api.apartment.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "apartments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "block_id", nullable = false)
    private UUID blockId;

    private String number;

    private Integer floor;

    private Double surface;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
