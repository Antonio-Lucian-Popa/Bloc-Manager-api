package com.asusoftware.BlocManager_api.repair_request.service;

import com.asusoftware.BlocManager_api.apartment.repository.ApartmentRepository;
import com.asusoftware.BlocManager_api.bloc.repository.BlocRepository;
import com.asusoftware.BlocManager_api.repair_request.model.RepairRequest;
import com.asusoftware.BlocManager_api.repair_request.repository.RepairRequestRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final BlocRepository blockRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public RepairRequest submitRequest(UUID blockId, UUID apartmentId, String description, UUID currentUserId) {
        if (!blockRepository.existsById(blockId)) {
            throw new RuntimeException("Blocul nu există.");
        }

        if (apartmentId != null && !apartmentRepository.existsById(apartmentId)) {
            throw new RuntimeException("Apartamentul nu există.");
        }

        RepairRequest request = RepairRequest.builder()
                .blockId(blockId)
                .apartmentId(apartmentId)
                .submittedBy(currentUserId)
                .description(description)
                .status("open")
                .createdAt(LocalDateTime.now())
                .build();

        return repairRequestRepository.save(request);
    }

    public List<RepairRequest> getRequestsForBlock(UUID blockId) {
        return repairRequestRepository.findAllByBlockId(blockId);
    }

    @Transactional
    public void updateStatus(UUID requestId, String newStatus) {
        RepairRequest request = repairRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Cererea nu există."));

        if (!List.of("open", "in_progress", "closed").contains(newStatus)) {
            throw new IllegalArgumentException("Status invalid.");
        }

        request.setStatus(newStatus);
        repairRequestRepository.save(request);
    }
}

