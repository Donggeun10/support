package com.example.support.repository;

import com.example.support.entity.AnnouncementFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementFileRespository extends JpaRepository<AnnouncementFile, Integer> {
    List<AnnouncementFile> findByAnnouncement_AnnounceId(String announceId);
    Optional<AnnouncementFile> findByAnnouncement_AnnounceIdAndFileId(String announceId, int fileId);
}
