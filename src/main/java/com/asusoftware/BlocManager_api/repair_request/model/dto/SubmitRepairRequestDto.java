package com.asusoftware.BlocManager_api.repair_request.model.dto;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SubmitRepairRequestDto {
    @NotNull
    private UUID blockId;

    private UUID apartmentId; // opțional

    @NotNull
    private String description;
}
