package com.example.support.repository;

import com.example.support.entity.Announcement;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface AnnouncementRespository extends JpaRepository<Announcement, Integer> {

    Optional<Announcement> findByAnnounceId(String announceId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Announcement c WHERE c.announceId = ?1")
    Optional<Announcement> fetchByAnnounceId(String announceId);

}
