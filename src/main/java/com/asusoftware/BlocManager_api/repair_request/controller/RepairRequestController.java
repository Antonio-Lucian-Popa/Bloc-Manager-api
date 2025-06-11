package com.asusoftware.BlocManager_api.repair_request.controller;

import com.asusoftware.BlocManager_api.repair_request.model.RepairRequest;
import com.asusoftware.BlocManager_api.repair_request.model.dto.SubmitRepairRequestDto;
import com.asusoftware.BlocManager_api.repair_request.service.RepairRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/repair-requests")
@RequiredArgsConstructor
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    @PostMapping
    public RepairRequest submitRequest(@RequestBody @Valid SubmitRepairRequestDto dto, Jwt principal) {
        UUID currentUserId = UUID.fromString(principal.getSubject());
        return repairRequestService.submitRequest(dto.getBlockId(), dto.getApartmentId(), dto.getDescription(), currentUserId);
    }

    @GetMapping("/block/{blockId}")
    public List<RepairRequest> getRequestsForBlock(@PathVariable UUID blockId) {
        return repairRequestService.getRequestsForBlock(blockId);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable UUID id, @RequestParam String status) {
        repairRequestService.updateStatus(id, status);
    }
}
