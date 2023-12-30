package com.example.demo.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class QuestionForm {

	@NotEmpty(message = "제목은 필수사항 입니다.")
	@Size(max=200)
	private String subject;
	
	
	@NotEmpty(message = "내용은 필수사항 입니다.")
	private String content;


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}
	
	
}
