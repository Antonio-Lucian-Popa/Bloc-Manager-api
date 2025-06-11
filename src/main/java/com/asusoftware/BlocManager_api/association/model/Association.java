package com.asusoftware.BlocManager_api.association.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "associations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Association {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String cif;

    private String address;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
