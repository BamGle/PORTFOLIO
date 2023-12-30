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
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	
	@PrePersist  // 디폴트 세팅할때 사용
	public void prePersist() {
		createDate = LocalDateTime.now();
	}	
	
	
	@ManyToOne
	private Question question;

	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
}
