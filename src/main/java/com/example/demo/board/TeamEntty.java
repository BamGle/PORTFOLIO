package com.example.demo.board;

import java.time.LocalDateTime;

import com.example.demo.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TeamEntty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// 제목
	@Column(length = 200)	
	private String subject;	
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	// 작성일
	private LocalDateTime createDate;
	private LocalDateTime modifyDate;
	
	@PrePersist  // 디폴트 세팅할때 사용
	public void prePersist() {
		createDate = LocalDateTime.now();
	}
	
	// 조회
	private long hit;	
	
	@ManyToOne
	private SiteUser author;

}
