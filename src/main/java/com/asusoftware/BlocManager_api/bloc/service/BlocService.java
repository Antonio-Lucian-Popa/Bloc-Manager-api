package com.asusoftware.BlocManager_api.bloc.service;

import com.asusoftware.BlocManager_api.association.model.Association;
import com.asusoftware.BlocManager_api.association.repository.AssociationRepository;
import com.asusoftware.BlocManager_api.bloc.model.Bloc;
import com.asusoftware.BlocManager_api.bloc.model.dto.BlocDto;
import com.asusoftware.BlocManager_api.bloc.model.dto.CreateBlocDto;
import com.asusoftware.BlocManager_api.bloc.repository.BlocRepository;
import com.asusoftware.BlocManager_api.user.model.User;
import com.asusoftware.BlocManager_api.user.model.UsersRole;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import com.asusoftware.BlocManager_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlocService {

    private final BlocRepository blockRepository;
    private final AssociationRepository associationRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    /**
     * Creează un bloc nou într-o asociație (doar pentru ADMIN_ASSOCIATION).
     */
    @Transactional
    public Bloc createBlock(UUID associationId, CreateBlocDto dto, Jwt currentUserId) {
        User currentUser = userService.getCurrentUserEntity(currentUserId);
        boolean isAdmin = userRoleRepository.existsByUserIdAndAssociationIdAndRole(
                currentUser.getId(), associationId, UsersRole.ADMIN_ASSOCIATION
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
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return blockRepository.save(block);
    }

    /**
     * Returnează blocurile dintr-o asociație, dacă userul are acces.
     */
    public Page<BlocDto> getBlocksByAssociation(UUID associationId, int page, int size, String search, Jwt principal) {
        User currentUser = userService.getCurrentUserEntity(principal);
        boolean hasAccess = userRoleRepository.existsByUserIdAndAssociationId(currentUser.getId(), associationId);
        if (!hasAccess) {
            throw new RuntimeException("Nu aveți acces la această asociație.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Bloc> blocksPage;
        if (search != null && !search.isBlank()) {
            blocksPage = blockRepository.findByAssociationIdAndNameContainingIgnoreCase(associationId, search, pageable);
        } else {
            blocksPage = blockRepository.findByAssociationId(associationId, pageable);
        }

        return blocksPage.map(block -> mapper.map(block, BlocDto.class));
    }

}
