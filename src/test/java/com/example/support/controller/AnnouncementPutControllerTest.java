package com.example.support.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.support.util.DataUtil;
import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
class AnnouncementPutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        final String userId = "robot";
        final String password = "play";

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
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    void testUpdateAnnouncementById() throws Exception {

        final String userId = "sam";
        final String password = "ground";

        mockMvc
            .perform(
                put("/api/v1/announcement/id/"+ DataUtil.getCurrentDatetimeStringValue()) // url
                    .param("title", "공지사항 제목3")
                    .param("content", "공지사항 내용3")
                    .param("beginDatetime", "2024-08-22 00:00:00")
                    .param("endDatetime", "2024-09-02 00:00:00")
                    .with(httpBasic(userId, password))
            )
            .andDo(print())
            .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("멀티 쓰레드를 통해서 동시성 문제에 대한 테스트")
    void testMultiThreadUpdateAnnouncementById() throws InterruptedException {

        int numberOfThreads = 10;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        try(ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < numberOfThreads; i++) {
                service.submit(()->{

                    final String userId = "sam";
                    final String password = "ground";

                    try {
                        String announceId = DataUtil.getCurrentDatetimeStringValue();

                        mockMvc
                            .perform(
                                put("/api/v1/announcement/id/"+ announceId) // url
                                    .param("title", "공지사항 제목 "+Thread.currentThread().threadId())
                                    .param("content", "공지사항 내용 "+System.currentTimeMillis())
                                    .param("beginDatetime", "2024-08-22 00:00:00")
                                    .param("endDatetime", "2024-09-02 00:00:00")
                                    .with(httpBasic(userId, password))
                            )
                            .andDo(print())
                            .andExpect(status().isAccepted());

                        mockMvc
                            .perform(
                                get("/api/v1/announcement/id/"+ announceId) // url
                                    .with(httpBasic(userId, password))
                            )
                            .andDo(print())
                            .andExpect(status().isOk());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    latch.countDown();
                });
            }
            latch.await();
        }

    }

}
