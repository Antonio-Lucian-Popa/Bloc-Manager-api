package com.asusoftware.BlocManager_api.repair_request.repository;

import com.asusoftware.BlocManager_api.repair_request.model.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, UUID> {
    List<RepairRequest> findByBlockId(UUID blockId);
    List<RepairRequest> findByApartmentId(UUID apartmentId);
    List<RepairRequest> findBySubmittedBy(UUID userId);
    List<RepairRequest> findAllByBlockId(UUID blockId);

}