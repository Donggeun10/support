package com.example.support.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.support.util.DataUtil;
import java.io.FileInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AnnouncementGetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String userId = "robot";
    private final String password = "play";


    @BeforeEach
    void setup() throws Exception {
        mockMvc
            .perform(
                post("/api/v1/announcement") // url
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .param("title", "공지사항 제목")
                    .param("content", "공지사항 내용")
                    .param("beginDatetime", "2024-08-22 00:00:00")
                    .param("endDatetime", "2024-09-22 00:00:00")
                    .with(httpBasic(userId,password))
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
                    .param("beginDatetime", "2024-08-22 00:00:00")
                    .param("endDatetime", "2024-09-22 00:00:00")
                    .with(httpBasic(userId,password))
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isCreated()); // response status 201 검증

        final String filePath3 = "src/test/resources/pdf/20231218_podClustering.pdf"; //파일경로
        FileInputStream fileInputStream3 = new FileInputStream(filePath3);

        //Mock파일생성
        MockMultipartFile pdf3 = new MockMultipartFile(
            "attached", //name
            "20231218_podClustering.pdf", //originalFilename
            "pdf",
            fileInputStream3
        );

        mockMvc
            .perform(
                multipart("/api/v1/announcement/id/"+DataUtil.getCurrentDatetimeStringValue()) // url
                    .file(pdf3)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .with(httpBasic(userId,password))
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isAccepted()); // response status 201 검증
    }

    @Test
    @DisplayName("모든 공지사항 조회 테스트")
    void testFoundAllAnnouncements() throws Exception {

        mockMvc
            .perform(
                get("/api/v1/announcements") // url
                    .with(httpBasic(userId,password))
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("공지사항 페이징에 대한 조회 테스트")
    void testFoundAnnouncementsByPaging() throws Exception {

        mockMvc
            .perform(
                get("/api/v1/announcements/page") // url
                    .param("page", "0")
                    .param("pageSize", "10")
                    .with(httpBasic(userId,password))
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("공지사항 페이징에 대한 조회 테스트 - 페이지에 해당하는 데이터가 없음")
    void testNotFoundAnnouncementsByPaging() throws Exception {

        mockMvc
            .perform(
                get("/api/v1/announcements/page") // url
                    .param("page", "2")
                    .param("pageSize", "10")
                    .with(httpBasic(userId,password))
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("공지사항 ID로 조회 테스트")
    void testFoundAnnouncementById() throws Exception {

        mockMvc
            .perform(
                get("/api/v1/announcement/id/"+ DataUtil.getCurrentDatetimeStringValue()) // url
                    .with(httpBasic(userId,password))
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("미 등록 사용자에 대한 미 인증 점검 테스트")
    void testFoundAnnouncementByIdUnauthorized() throws Exception {

        mockMvc
            .perform(
                get("/api/v1/announcement/id/"+ DataUtil.getCurrentDatetimeStringValue()) // url
                    .with(httpBasic("superuser","notepad"))
            )
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
}
