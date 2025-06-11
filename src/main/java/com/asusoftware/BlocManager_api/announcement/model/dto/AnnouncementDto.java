package com.asusoftware.BlocManager_api.announcement.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDto {
    private UUID id;
    private UUID blockId;
    private String title;
    private String message;
    private UUID postedBy;
    private LocalDateTime createdAt;
}
