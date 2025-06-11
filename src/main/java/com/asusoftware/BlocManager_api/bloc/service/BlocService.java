package com.asusoftware.BlocManager_api.bloc.service;

import com.asusoftware.BlocManager_api.association.model.Association;
import com.asusoftware.BlocManager_api.association.repository.AssociationRepository;
import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import com.asusoftware.BlocManager_api.bloc.model.dto.CreateBlocDto;
import com.asusoftware.BlocManager_api.bloc.repository.BlocRepository;
import com.asusoftware.BlocManager_api.user.model.UsersRole;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlocService {

    private final BlocRepository blockRepository;
    private final AssociationRepository associationRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * Creează un bloc nou într-o asociație (doar pentru ADMIN_ASSOCIATION).
     */
    @Transactional
    public Bloc createBlock(UUID associationId, CreateBlocDto dto, UUID currentUserId) {
        boolean isAdmin = userRoleRepository.existsByUserIdAndAssociationIdAndRole(
                currentUserId, associationId, UsersRole.ADMIN_ASSOCIATION
        );
        if (!isAdmin) {
            throw new RuntimeException("Doar adminul asociației poate adăuga blocuri.");
        }

        Association association = associationRepository.findById(associationId)
                .orElseThrow(() -> new RuntimeException("Asociația nu există"));

        Bloc block = Bloc.builder()
                .associationId(associationId)
                .name(dto.getName())
                .address(dto.getAddress())
                .build();

        return blockRepository.save(block);
    }

    /**
     * Returnează blocurile dintr-o asociație, dacă userul are acces.
     */
    public List<Bloc> getBlocksByAssociation(UUID associationId, UUID currentUserId) {
        boolean hasAccess = userRoleRepository.existsByUserIdAndAssociationId(currentUserId, associationId);
        if (!hasAccess) {
            throw new RuntimeException("Nu aveți acces la această asociație.");
        }

        return blockRepository.findAllByAssociationId(associationId);
    }
}
