package com.asusoftware.BlocManager_api.association.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssociationDto {
    private String name;
    private String cif;
    private String address;
}
