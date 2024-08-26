package com.example.support.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="TB_ANNOUNCEMENT_FILE")
@ToString
public class AnnouncementFile implements Serializable { // 제목, 내용, 작성자, 공지 시작일시, 공지 종료일시, 첨부파일(여러개), 등록잀시, 조회수

	@Id
	@JsonProperty
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	@Getter
	private int fileId;

	@JsonProperty
	@Column(name = "name", length = 200)
	private String name;

	@Getter
	@JsonProperty
	@Column(name = "path", length = 200)
	private String path;

	@JsonProperty
	@CreationTimestamp
	@Column(name = "inserted_datetime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp insertedDatetime;

	@ManyToOne
	@JoinColumn(name = "announce_id")
	private Announcement announcement;


}
	

