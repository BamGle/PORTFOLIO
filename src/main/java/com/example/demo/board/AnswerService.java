package com.example.demo.board;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DataNotFoundException;
import com.example.demo.user.SiteUser;

@Service
public class AnswerService {

	@Autowired
	private AnswerRepository answerRepository;
	
	public void create(Question question, String content,SiteUser siteUser) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(siteUser);
		answerRepository.save(answer);
		// TODO Auto-generated method stub
		
	}
	public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
    
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

}
