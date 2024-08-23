package com.example.support.domain;

import com.example.support.entity.AnnouncementFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnnouncementResponse {
    // 제목, 내용, 등록일시, 조회수, 작성자,
    private String announceId;
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp insertedDatetime;
    private int viewCount;
    private String writer;
    private List<AnnouncementFile> attachedFiles;

}
