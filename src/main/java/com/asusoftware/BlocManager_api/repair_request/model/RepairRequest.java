package com.asusoftware.BlocManager_api.repair_request.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "repair_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairRequest {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "apartment_id")
    private UUID apartmentId;

    @Column(name = "block_id", nullable = false)
    private UUID blockId;

    @Column(name = "submitted_by")
    private UUID submittedBy;

    private String description;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
