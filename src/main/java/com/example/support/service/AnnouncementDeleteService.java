package com.example.support.service;

import com.example.support.component.FileManager;
import com.example.support.entity.Announcement;
import com.example.support.entity.AnnouncementFile;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.repository.AnnouncementFileRespository;
import com.example.support.repository.AnnouncementRespository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AnnouncementDeleteService {

	private final AnnouncementRespository announcementRespository;
	private final AnnouncementFileRespository announcementFileRespository;
	private final FileManager fileManager;

	@Transactional
	public void deleteAnnouncementById(String id) {

		Optional<Announcement> data = announcementRespository.fetchByAnnounceId(id);
		if(data.isPresent()){
			List<AnnouncementFile> files = announcementFileRespository.findByAnnouncement_AnnounceId(id);
			announcementRespository.delete(data.get());
			fileManager.deleteAnnouncementFiles(id, files);
		}else{
			throw new NotFoundAnnouncementException(String.format("요청한 ID(%s)의 공지 사항 정보가 없습니다.", id));
		}
	}

	public void deleteAnnouncementByIdAndFileId(String id, int fileId) {

		Optional<Announcement> data = announcementRespository.findByAnnounceId(id);
		if(data.isPresent()){
			Optional<AnnouncementFile> fileOpt = announcementFileRespository.findByAnnouncement_AnnounceIdAndFileId(id, fileId);
			if(fileOpt.isPresent()){
				AnnouncementFile file = fileOpt.get();
				announcementFileRespository.delete(file);
				fileManager.deleteAnnouncementFile(file);
			}else{
				throw new NotFoundAnnouncementException(String.format("요청한 ID(%s)의 공지 사항의 파일(%s) 정보가 없습니다.", id, fileId));
			}
		}else{
			throw new NotFoundAnnouncementException(String.format("요청한 ID(%s)의 공지 사항 정보가 없습니다.", id));
		}
	}

}
