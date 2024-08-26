package com.example.support.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="TB_ANNOUNCEMENT",
		indexes={
			@Index(name = "idx_announce_n1", columnList = "begin_datetime")
})
@ToString
@Getter
public class Announcement implements Serializable { // 제목, 내용, 작성자, 공지 시작일시, 공지 종료일시, 첨부파일(여러개), 등록잀시, 조회수

	@Id
	@JsonProperty
	@Column(name = "announce_id")
	private String announceId;

	@JsonProperty
	@Column(name = "title", length = 200)
	private String title;

	@JsonProperty
	@Column(name = "content", length = 2000)
	private String content;
	
	@JsonProperty
	@Column(name = "view_count")
	private int viewCount;

	@JsonProperty
	@Column(name = "writer" , length = 20)
	private String writer;
	
	@JsonProperty
	@CreationTimestamp
	@Column(name = "inserted_datetime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp insertedDatetime;

	@JsonProperty
	@UpdateTimestamp
	@Column(name = "updated_datetime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp updatedDatetime;

	@JsonProperty
	@Column(name = "begin_datetime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp beginDatetime;

	@JsonProperty
	@Column(name = "end_datetime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp endDatetime;

	@OneToMany(mappedBy = "announcement", fetch = FetchType.EAGER)
	private List<AnnouncementFile> files = new ArrayList<>();

	public void increaseViewCount() {
		this.viewCount++;
	}

	public void update(Announcement newAnnouncement){
		this.title = newAnnouncement.getTitle();
		this.content = newAnnouncement.getContent();
		this.writer = newAnnouncement.getWriter();
		this.beginDatetime = newAnnouncement.getBeginDatetime();
		this.endDatetime = newAnnouncement.getEndDatetime();
	}

}
	

