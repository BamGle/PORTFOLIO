package com.example.demo.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.DataNotFoundException;
import com.example.demo.user.SiteUser;

//@RequiredArgsConstructor
@Service
public class QuestionService {
	@Autowired
	private QuestionRepository qeustionRepository;  // 자동주입
	@Autowired
	private FindTeamRepository findTeamRepository; 
	
	
	public Page<Question> getList(int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate")); // 내림차순으로 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return qeustionRepository.findAll(pageable);
	}
	
	
	public Question detail(Integer id) {
		Optional<Question> oq = qeustionRepository.findById(id);
		if(oq.isPresent())
			return oq.get();
		else
			throw new DataNotFoundException("questioni not found");		
	}

	
	
	
	public void create(String subject, String content, SiteUser siteUser) {
		Question q = new Question();		
		q.setSubject(subject);
		q.setContent(content);
		q.setAuthor(siteUser);
		qeustionRepository.save(q);
	}
	
	public void modify(Question question, String subject, String content) {		
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
//		question.setCreateDate(LocalDateTime.now());
		qeustionRepository.save(question);
	}


	public void delete(Question question) {
		qeustionRepository.delete(question);		
	}


	
	// create file team 
		public void createfindTeam(String subject, String content, SiteUser siteUser) {
			TeamEntty t = new TeamEntty();
			t.setSubject(subject);
			t.setContent(content);
			t.setAuthor(siteUser);
			findTeamRepository.save(t);
		}
		// 팀원 찾기 리스트
		public List<TeamEntty> findTeamList(){			
			return findTeamRepository.findAll( Sort.by(Sort.Order.desc("createDate"))  );
		}
		public TeamEntty teamDetail(int id) {
			Optional<TeamEntty> oq = findTeamRepository.findById(id);
			if(oq.isPresent())
				return oq.get();
			else
				throw new DataNotFoundException("team not found");
		}

		public void modify(TeamEntty teamEntry , String subject, String content) {
			teamEntry.setSubject(subject);
			teamEntry.setContent(content);
			teamEntry.setModifyDate(LocalDateTime.now());
			findTeamRepository.save(teamEntry);
			
		}
		
		public void teamEntryDelete(TeamEntty teamEntry) {
			findTeamRepository.delete(teamEntry);		
		}

}
