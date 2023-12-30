package com.example.demo.board;

import jakarta.validation.constraints.NotEmpty;

public class AnswerForm {
	@NotEmpty(message = "내용은 필수사항 입니다.")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}


