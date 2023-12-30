package com.example.demo.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FindTeamRepository extends JpaRepository<TeamEntty, Integer>{
	TeamEntty findBySubject(String subject);
	TeamEntty findBySubjectAndContent(String subject,String content);
	List<TeamEntty> findBySubjectLike(String subject);
	Page<TeamEntty> findAll(Pageable pageable);

}
