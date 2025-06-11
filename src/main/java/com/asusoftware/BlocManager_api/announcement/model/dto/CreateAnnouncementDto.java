package com.asusoftware.BlocManager_api.announcement.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnnouncementDto {
    private UUID blockId;
    private String title;
    private String message;
    private UUID postedBy;
}
