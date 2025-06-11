package com.asusoftware.BlocManager_api.bloc.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlocDto {
    private UUID id;
    private UUID associationId;
    private String name;
    private String address;
    private LocalDateTime createdAt;
}
