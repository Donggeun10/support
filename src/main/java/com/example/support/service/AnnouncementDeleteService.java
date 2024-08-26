package com.example.support.service;

import com.example.support.component.FileManager;
import com.example.support.entity.Announcement;
import com.example.support.entity.AnnouncementFile;
import com.example.support.exception.NotFoundAnnouncementException;
import com.example.support.repository.AnnouncementFileRespository;
import com.example.support.repository.AnnouncementRespository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AnnouncementDeleteService {

	private final AnnouncementRespository announcementRespository;
	private final AnnouncementFileRespository announcementFileRespository;
	private final FileManager fileManager;

    public AnnouncementDeleteService(AnnouncementRespository announcementRespository, AnnouncementFileRespository announcementFileRespository, FileManager fileManager) {
        this.announcementRespository = announcementRespository;
        this.announcementFileRespository = announcementFileRespository;
        this.fileManager = fileManager;
    }

	@CacheEvict(cacheNames = "apps", key = "#id")
	@Transactional
	public void deleteAnnouncementById(String id) throws NotFoundAnnouncementException {

		Optional<Announcement> data = announcementRespository.findByAnnounceId(id);
		if(data.isPresent()){
			List<AnnouncementFile> files = announcementFileRespository.findByAnnouncement_AnnounceId(id);
			announcementFileRespository.deleteAll(files);
			announcementRespository.delete(data.get());
			fileManager.deleteAnnouncementFiles(id, files);
		}else{
			throw new NotFoundAnnouncementException(String.format("요청한 ID(%s)의 공지 사항 정보가 없습니다.", id));
		}
	}

	@CacheEvict(cacheNames = "apps", key = "#id")
	public void deleteAnnouncementByIdAndFileId(String id, int fileId) throws NotFoundAnnouncementException {

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
