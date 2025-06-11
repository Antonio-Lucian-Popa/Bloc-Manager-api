package com.asusoftware.BlocManager_api.announcement.repository;

import com.asusoftware.BlocManager_api.announcement.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {
    List<Announcement> findByBlockId(UUID blockId);
}
