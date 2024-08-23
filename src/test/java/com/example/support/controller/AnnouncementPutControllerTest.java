package com.example.support.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.support.util.DataUtil;
import java.io.FileInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AnnouncementPutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc
            .perform(
                post("/api/v1/announcement") // url
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("title", "공지사항 제목")
                    .param("content", "공지사항 내용")
                    .param("writer", "작성자")
                    .param("beginDatetime", "2024-08-22 00:00:00")
                    .param("endDatetime", "2024-09-22 00:00:00")
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isCreated()); // response status 201 검증

        Thread.sleep(1000);
        final String filePath1 = "src/test/resources/pdf/20240202_CDS.pdf"; //파일경로
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);

        //Mock파일생성
        MockMultipartFile pdf1 = new MockMultipartFile(
            "attached", //name
            "20240202_CDS.pdf", //originalFilename
            "pdf",
            fileInputStream1
        );

        final String filePath2 = "src/test/resources/pdf/20240229_ollama.pdf"; //파일경로
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);

        //Mock파일생성
        MockMultipartFile pdf2 = new MockMultipartFile(
            "attached", //name
            "20240229_ollama.pdf", //originalFilename
            "pdf",
            fileInputStream2
        );

        mockMvc
            .perform(
                multipart("/api/v1/announcement") // url
                    .file(pdf1)
                    .file(pdf2)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("title", "공지사항 제목2")
                    .param("content", "공지사항 내용2")
                    .param("writer", "작성자2")
                    .param("beginDatetime", "2024-08-22 00:00:00")
                    .param("endDatetime", "2024-09-22 00:00:00")
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isCreated()); // response status 201 검증
    }

    @Test
    void testUpdateAnnouncementById() throws Exception {

        mockMvc
            .perform(
                put("/api/v1/announcement/id/"+ DataUtil.getCurrentDatetimeStringValue()) // url
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("title", "공지사항 제목3")
                    .param("content", "공지사항 내용3")
                    .param("writer", "작성자3")
                    .param("beginDatetime", "2024-08-22 00:00:00")
                    .param("endDatetime", "2024-09-02 00:00:00")
            )
            .andDo(print())
            .andExpect(status().isAccepted());
    }

}