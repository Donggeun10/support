package com.example.support.repository;

import com.example.support.entity.Announcement;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface AnnouncementRespository extends JpaRepository<Announcement, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Announcement> findByAnnounceId(String announceId);

}
