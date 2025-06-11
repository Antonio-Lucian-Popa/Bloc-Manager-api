package com.asusoftware.BlocManager_api.announcement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "announcements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "block_id", nullable = false)
    private UUID blockId;

    private String title;

    private String message;

    @Column(name = "posted_by")
    private UUID postedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
