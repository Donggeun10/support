package com.example.support.component;

import com.example.support.entity.AnnouncementFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FileManager {

    @Value("${application.announcement.attachment.upload-dir}")
    private String uploadDir;

    public String saveAnnouncementFile(String announcementId, MultipartFile file) throws IOException {
        log.debug("{}:{}:{}", uploadDir, announcementId, file.getOriginalFilename());

        String targetFileName = "";
        File targetDir = Path.of(uploadDir, announcementId).toFile();

        if(targetDir.isDirectory() || targetDir.mkdirs()){
            File targetFile = Path.of(uploadDir, announcementId, file.getOriginalFilename()).toFile();
            try(InputStream initialStream = file.getInputStream()){
                Files.copy(
                    initialStream,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            }
            targetFileName = targetFile.getPath();
        } else {
            log.warn("mkdir cannot {} ", targetDir);
        }

        return targetFileName;
    }

    public void deleteAnnouncementFiles(String announcementId, List<AnnouncementFile> files) {

        for(AnnouncementFile file : files) {
            try {
                Files.delete(Path.of(file.getPath()));
            } catch (IOException e) {
                log.error("파일 삭제 실패: {}", file.getPath());
            }
        }

        Path targetDir = Path.of(uploadDir, announcementId);
        if(targetDir.toFile().isDirectory()){
            try {
                Files.delete(targetDir);
            } catch (IOException e) {
                log.error("폴더 삭제 실패: {}", targetDir);
            }
        }
    }

    public void deleteAnnouncementFile(AnnouncementFile file) {
        try {
            Files.delete(Path.of(file.getPath()));
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", file.getPath());
        }
    }
}
