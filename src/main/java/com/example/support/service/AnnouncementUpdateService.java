package com.example.support.service;


import com.example.support.entity.Announcement;
import com.example.support.exception.DataSaveException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.repository.AnnouncementRespository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AnnouncementUpdateService {

	private final AnnouncementRespository announcementRespository;

    @Transactional(rollbackFor = {NotFoundAnnouncementException.class, DataSaveException.class})
	public void updateAnnouncementById(String announcementId, Announcement newAnnouncement) {

		Optional<Announcement> announcementOpt = announcementRespository.fetchByAnnounceId(announcementId);

		if (announcementOpt.isPresent()) {
			try {
				Announcement oldAnnouncement = announcementOpt.get();
				oldAnnouncement.update(newAnnouncement);
				announcementRespository.save(oldAnnouncement);
			} catch (Exception e) {
				throw new DataSaveException(e.getMessage());
			}
		} else {
			throw new NotFoundAnnouncementException(String.format("요청한 ID(%s)의 공지 사항 정보가 없습니다.", announcementId));
		}

	}

}
