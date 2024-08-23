package com.example.support.component;

import com.example.support.entity.AnnouncementFile;
import com.example.support.util.DataUtil;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class FileManagerTest {

    @Value("${application.announcement.attachment.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileManager fileManager;
    private final String announcementId = DataUtil.getCurrentDatetimeStringValue(); //공지사항ID
    private final String fileName = "20240229_ollama.pdf"; //파일명

    @BeforeEach
    void setup() throws Exception {

        final String filePath1 = "src/test/resources/pdf/"+fileName; //파일경로
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);

        //Mock파일생성
        MockMultipartFile pdf1 = new MockMultipartFile(
            "attached", //name
            fileName, //originalFilename
            "pdf",
            fileInputStream1
        );

        String result = fileManager.saveAnnouncementFile(announcementId, pdf1);
        Assertions.assertEquals(result, Path.of(uploadDir, announcementId, fileName).toString());
    }

    @Test
    void testSaveAnnouncementFile() throws Exception {

        final String fileName1 = "20240202_CDS.pdf"; //파일명
        final String filePath1 = "src/test/resources/pdf/"+fileName1; //파일경로
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);

        //Mock파일생성
        MockMultipartFile pdf1 = new MockMultipartFile(
            "attached", //name
            fileName1, //originalFilename
            "pdf",
            fileInputStream1
        );

        String result = fileManager.saveAnnouncementFile(announcementId, pdf1);
        Assertions.assertEquals(result, Path.of(uploadDir, announcementId, fileName1).toString());

    }

    @Test
    void testDeleteAnnouncementFile() {

        AnnouncementFile announcementFile = AnnouncementFile.builder()
            .path(Path.of(uploadDir,announcementId, fileName).toString())
            .build();

        fileManager.deleteAnnouncementFile(announcementFile);

    }

    @Test
    void testDeleteAnnouncementFiles() {

        List<AnnouncementFile> announcementFiles = new ArrayList<>();

        AnnouncementFile announcementFile = AnnouncementFile.builder()
            .path(Path.of(uploadDir,announcementId, fileName).toString())
            .build();

        announcementFiles.add(announcementFile);

        fileManager.deleteAnnouncementFiles(announcementId, announcementFiles);

    }
}
