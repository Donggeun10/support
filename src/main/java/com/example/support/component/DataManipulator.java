package com.example.support.component;

import com.example.support.domain.AnnouncementResponse;
import com.example.support.entity.Announcement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataManipulator {

    public AnnouncementResponse makeAnnouncementResponse(Announcement announcement) {
        return AnnouncementResponse.builder()
                .announceId(announcement.getAnnounceId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .insertedDatetime(announcement.getInsertedDatetime())
                .viewCount(announcement.getViewCount())
                .writer(announcement.getWriter())
                .attachedFiles(announcement.getFiles())
                .build();
    }

    public List<AnnouncementResponse> makeAnnouncementResponseList(List<Announcement> announcements) {
        return announcements.stream()
                .map(this::makeAnnouncementResponse)
                .toList();
    }
}
