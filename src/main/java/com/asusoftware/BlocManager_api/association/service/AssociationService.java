package com.asusoftware.BlocManager_api.association.service;

import com.asusoftware.BlocManager_api.association.model.Association;
import com.asusoftware.BlocManager_api.association.model.dto.AssociationDto;
import com.asusoftware.BlocManager_api.association.model.dto.CreateAssociationDto;
import com.asusoftware.BlocManager_api.association.repository.AssociationRepository;
import com.asusoftware.BlocManager_api.user.model.User;
import com.asusoftware.BlocManager_api.user.model.UserRole;
import com.asusoftware.BlocManager_api.user.model.UsersRole;
import com.asusoftware.BlocManager_api.user.model.dto.UserDto;
import com.asusoftware.BlocManager_api.user.model.dto.UserRoleDto;
import com.asusoftware.BlocManager_api.user.repository.UserRepository;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import com.asusoftware.BlocManager_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssociationService {

    private final AssociationRepository associationRepository;
    private final UserService userService;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    /**
     * Creează o nouă asociație (doar pentru utilizator cu rol ADMIN_ASSOCIATION).
     */
    @Transactional
    public AssociationDto createAssociation(CreateAssociationDto dto, Jwt principal) {
        User currentUser = userService.getCurrentUserEntity(principal);

        UserRole userRole = userRoleRepository.findByUserId(currentUser.getId()).orElseThrow(NotFoundException::new);

        // Poți valida dacă userul are deja o asociație creată (dacă vrei să limitezi)
        boolean hasRole = userRoleRepository.existsByUserIdAndRole(currentUser.getId(), UsersRole.ADMIN_ASSOCIATION);
        if (!hasRole) {
            throw new RuntimeException("Nu ai permisiunea să creezi o asociație.");
        }

        Association association = Association.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .cif(dto.getCif())
                .createdBy(currentUser.getId())
                .build();

        associationRepository.save(association);

        userRole.setAssociationId(association.getId());

        userRoleRepository.save(userRole);

        return mapper.map(association, AssociationDto.class);
    }

    @Transactional
    public void inviteUserToAssociation(UUID associationId, String email, UsersRole role, UUID blocId, Jwt principal) {
        // 1. Verifică dacă cel care invită este admin în asociație
        User currentUser = userService.getCurrentUserEntity(principal);
        boolean isAdmin = userRoleRepository.existsByUserIdAndAssociationIdAndRole(
                currentUser.getId(), associationId, UsersRole.ADMIN_ASSOCIATION
        );
        if (!isAdmin) {
            throw new AccessDeniedException("Nu ai permisiunea să inviți utilizatori în această asociație.");
        }

        // 2. Verifică dacă utilizatorul există
        User invitedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilizatorul invitat nu există."));

        // 3. Verifică dacă deja are rolul respectiv
        boolean alreadyExists = userRoleRepository.existsByUserIdAndAssociationIdAndRole(
                invitedUser.getId(), associationId, role
        );
        if (alreadyExists) {
            throw new RuntimeException("Utilizatorul are deja acest rol în asociație.");
        }

        // 4. Creează rolul
        UserRole userRole = UserRole.builder()
                .userId(invitedUser.getId())
                .associationId(associationId)
                .role(role)
                .build();
        userRoleRepository.save(userRole);
    }


    /**
     * Returnează toate asociațiile (ex: pentru super-admin) – poți filtra ulterior.
     */
    public List<AssociationDto> getAllAssociations() {
        return associationRepository.findAll().stream()
                .map(association -> mapper.map(association, AssociationDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Returnează toate asociațiile create de utilizatorul curent.
     */
    public Page<AssociationDto> getMyAssociations(Jwt principal, int page, int size, String search) {
        UUID currentUserId = userService.getUserByKeycloakId(principal);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Association> associations;

        if (search != null && !search.isBlank()) {
            associations = associationRepository.findByCreatedByAndNameContainingIgnoreCase(currentUserId, search, pageable);
        } else {
            associations = associationRepository.findByCreatedBy(currentUserId, pageable);
        }

        return associations.map(association -> mapper.map(association, AssociationDto.class));
    }


    /**
     * Găsește o asociație după ID.
     */
    public AssociationDto getById(UUID id) {
        Association association = associationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asociația nu a fost găsită"));
        return mapper.map(association, AssociationDto.class);
    }

    public Page<UserDto> getUsersByAssociation(UUID associationId, int page, int size, String search, Jwt principal) {
        // 1. Verificăm dacă userul are acces la asociație
        User currentUser = userService.getCurrentUserEntity(principal);
        boolean hasAccess = userRoleRepository.existsByUserIdAndAssociationId(currentUser.getId(), associationId);
        if (!hasAccess) {
            throw new AccessDeniedException("Nu ai acces la această asociație.");
        }

        Pageable pageable = PageRequest.of(page, size);

        // 2. Obținem toți userii cu roluri în această asociație (filtrați direct dacă e căutare)
        Page<User> usersPage;

        if (search != null && !search.isBlank()) {
            usersPage = userRepository.findDistinctByIdInAndSearch(
                    associationId, search.toLowerCase(), pageable
            );
        } else {
            usersPage = userRepository.findDistinctByIdIn(
                    associationId, pageable
            );
        }

        // 3. Preluăm toate rolurile în acea asociație pentru userii returnați
        List<UUID> userIds = usersPage.stream().map(User::getId).toList();
        List<UserRole> roles = userRoleRepository.findByAssociationIdAndUserIdIn(associationId, userIds);

        Map<UUID, UserRole> roleMap = roles.stream()
                .collect(Collectors.toMap(UserRole::getUserId, role -> role, (a, b) -> a));

        // 4. Mapăm în DTO
        return usersPage.map(user -> {
            UserDto dto = mapper.map(user, UserDto.class);
            UserRole role = roleMap.get(user.getId());
            if (role != null) {
                dto.setRole(mapper.map(role, UserRoleDto.class));
            }
            return dto;
        });
    }



}
