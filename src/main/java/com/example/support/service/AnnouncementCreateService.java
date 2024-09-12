package com.example.support.service;

import com.example.support.component.FileManager;
import com.example.support.entity.Announcement;
import com.example.support.entity.AnnouncementFile;
import com.example.support.exception.DataSaveException;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.repository.AnnouncementFileRespository;
import com.example.support.repository.AnnouncementRespository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@Service
public class AnnouncementCreateService {

	private final AnnouncementRespository announcementRespository;
	private final AnnouncementFileRespository announcementFileRespository;
	private final FileManager fileManager;

    @CacheEvict(value = "apps", allEntries = true)
	@Transactional
    public void insertAnnouncement(Announcement announcement, List<MultipartFile> files) {
		
		try {
			if(isNoAttachedFiles(files)) {
				return;
			}

			List<AnnouncementFile> announcementFiles = new ArrayList<>();
			for(MultipartFile file : files) {
				AnnouncementFile announcementFile = AnnouncementFile.builder()
						.name(file.getOriginalFilename())
						.announcement(announcement)
						.path(fileManager.saveAnnouncementFile(announcement.getAnnounceId(), file))
						.build();
				announcementFiles.add(announcementFile);
			}
			announcement.setFiles(announcementFiles);

			announcementRespository.save(announcement);

		} catch(Exception e) {
			throw new DataSaveException(e.getMessage());
		}
	}

	private boolean isNoAttachedFiles(List<MultipartFile> files) {
		return files == null || files.isEmpty();
	}

	public void appendFilesByAnnouncementId(String announcementId, List<MultipartFile> files) {

		try {

			List<AnnouncementFile> announcementFiles = new ArrayList<>();
			Announcement announcement = announcementRespository.findByAnnounceId(announcementId).orElseThrow(() -> new NotFoundAnnouncementException("Not found announcement"));

			for(MultipartFile file : files) {
				AnnouncementFile announcementFile = AnnouncementFile.builder()
					.name(file.getOriginalFilename())
					.announcement(announcement)
					.path(fileManager.saveAnnouncementFile(announcement.getAnnounceId(), file))
					.build();
				announcementFiles.add(announcementFile);
			}

			announcementFileRespository.saveAll(announcementFiles);

		} catch(Exception e) {
			throw new DataSaveException(e.getMessage());
		}
	}

}
