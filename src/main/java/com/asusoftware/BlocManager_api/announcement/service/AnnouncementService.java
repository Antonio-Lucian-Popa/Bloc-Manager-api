package com.asusoftware.BlocManager_api.announcement.service;

import com.asusoftware.BlocManager_api.announcement.model.Announcement;
import com.asusoftware.BlocManager_api.announcement.model.dto.AnnouncementDto;
import com.asusoftware.BlocManager_api.announcement.model.dto.CreateAnnouncementDto;
import com.asusoftware.BlocManager_api.announcement.repository.AnnouncementRepository;
import com.asusoftware.BlocManager_api.user.model.User;
import com.asusoftware.BlocManager_api.user.repository.UserRoleRepository;
import com.asusoftware.BlocManager_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    /**
     * Creează un anunț nou în cadrul unei asociații/bloc.
     */
    @Transactional
    public AnnouncementDto createAnnouncement(CreateAnnouncementDto dto, Jwt principal) {
        User currentUser = userService.getCurrentUserEntity(principal);

        // Verifică dacă userul are acces la blocul specificat
        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(currentUser.getId(), dto.getBlockId());
        if (!hasAccess) {
            throw new RuntimeException("Nu ai permisiunea de a publica anunțuri pentru acest bloc.");
        }

        Announcement announcement = Announcement.builder()
                .blockId(dto.getBlockId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .postedBy(currentUser.getId())
                .build();

        announcementRepository.save(announcement);
        return mapper.map(announcement, AnnouncementDto.class);
    }

    /**
     * Listează toate anunțurile pentru un bloc.
     */
    public List<AnnouncementDto> getAllForBlock(UUID blockId, Jwt principal) {
        UUID userId = userService.getUserByKeycloakId(principal);

        boolean hasAccess = userRoleRepository.existsByUserIdAndBlockId(userId, blockId);
        if (!hasAccess) {
            throw new RuntimeException("Acces interzis la anunțurile acestui bloc.");
        }

        return announcementRepository.findAllByBlockId(blockId).stream()
                .map(a -> mapper.map(a, AnnouncementDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Șterge un anunț (doar dacă e creatorul sau are rol de administrator).
     */
    @Transactional
    public void deleteAnnouncement(UUID announcementId, Jwt principal) {
        UUID userId = userService.getUserByKeycloakId(principal);
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Anunțul nu există."));

        boolean isAdmin = userRoleRepository.existsByUserIdAndBlockId(userId, announcement.getBlockId());
        boolean isOwner = announcement.getPostedBy().equals(userId);

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("Nu ai dreptul să ștergi acest anunț.");
        }

        announcementRepository.deleteById(announcementId);
    }
}