package com.asusoftware.BlocManager_api.bloc.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blocks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bloc {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "association_id", nullable = false)
    private UUID associationId;

    private String name;

    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
