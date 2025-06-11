package com.asusoftware.BlocManager_api.bloc.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlocDto {
    private UUID associationId;
    private String name;
    private String address;
}
