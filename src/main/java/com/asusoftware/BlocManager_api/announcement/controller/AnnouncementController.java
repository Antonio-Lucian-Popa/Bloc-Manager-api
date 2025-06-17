package com.asusoftware.BlocManager_api.announcement.controller;

import com.asusoftware.BlocManager_api.announcement.model.dto.AnnouncementDto;
import com.asusoftware.BlocManager_api.announcement.model.dto.CreateAnnouncementDto;
import com.asusoftware.BlocManager_api.announcement.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * Creează un nou anunț într-un bloc.
     */
    @PostMapping
    public AnnouncementDto createAnnouncement(@RequestBody @Valid CreateAnnouncementDto dto,
                                              @AuthenticationPrincipal Jwt principal) {
        return announcementService.createAnnouncement(dto, principal);
    }

    /**
     * Returnează toate anunțurile pentru un bloc.
     */
    // TODO: De pus pageable
    @GetMapping("/block/{blockId}")
    public List<AnnouncementDto> getAnnouncementsForBlock(@PathVariable UUID blockId,
                                                          @AuthenticationPrincipal Jwt principal) {
        return announcementService.getAllForBlock(blockId, principal);
    }

    /**
     * Șterge un anunț după ID.
     */
    @DeleteMapping("/{id}")
    public void deleteAnnouncement(@PathVariable UUID id,
                                   @AuthenticationPrincipal Jwt principal) {
        announcementService.deleteAnnouncement(id, principal);
    }
}

