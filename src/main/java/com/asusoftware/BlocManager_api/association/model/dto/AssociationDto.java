package com.asusoftware.BlocManager_api.association.model.dto;

import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociationDto {
    private UUID id;
    private String name;
    private String cif;
    private String address;
    private LocalDateTime createdAt;
}
