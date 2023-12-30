package com.example.demo.board;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.user.SiteUser;
import com.example.demo.user.UserService;

import jakarta.validation.Valid;

//@RequiredArgsConstructor

@Controller
@RequestMapping("/question")
public class QuestionController {
	@Autowired
	private QuestionService questionService;	
	@Autowired
	private UserService userService;
	
	
	
	
	// 팀원 찾기
	@GetMapping("/findTeam")
	public String findTeam(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
		model.addAttribute("BoardDTO", questionService.findTeamList() );
		return "board/find_team";
	}
	
	// 팀원에서 글 입력하기
	@GetMapping("/create_find_team")
	public String create_find_team(Model model,Principal principal,FindTeamForm findTeamFrom) {
		model.addAttribute("sessionId", principal.getName());
		return "board/create_find_team";
	}
	
	// 글 등록하기
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create_find_team")
	public String create_find_team_post(@Valid FindTeamForm findTeamForm,BindingResult bindingResult,Principal principal) {
		if(bindingResult.hasErrors())
			return "board/create_find_team";		
//		저장
		SiteUser siteUser = userService.getUser(principal.getName());
		questionService.createfindTeam(findTeamForm.getSubject(), findTeamForm.getContent(), siteUser);
		return "redirect:/question/findTeam";
	}
	// 팀원의 상세내용
	@GetMapping(value = "/findTeam/detail/{id}")
	public String findTeamDetail(Model model,@PathVariable("id") Integer id, FindTeamForm findTeamForm) {
		TeamEntty t =  questionService.teamDetail(id);		
		model.addAttribute("teamEntry",t);		
		return "board/team_detail";
	}
	// 팀원찾기 게시판 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/findTeam/detail/modify")
	public String findTeamDetailModify(FindTeamForm findTeamForm, BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors())
			return "board/team_detail";	
		TeamEntty teamEntry = this.questionService.teamDetail(Integer.valueOf(findTeamForm.getId()));
        if (!teamEntry.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(teamEntry, findTeamForm.getSubject(), findTeamForm.getContent());
        return String.format("redirect:/question/findTeam/detail/%s", teamEntry.getId());
	}	
	
	// 팀원 찾기 게시판 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/findTeam/delete/{id}")
	 public String teamEntryDelete(Principal principal, @PathVariable("id") Integer id) {
		TeamEntty teamEntry = this.questionService.teamDetail(id);
        if (!teamEntry.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.teamEntryDelete(teamEntry);
        return "redirect:/";
    }
	
	
	
	
	
	@GetMapping("/list")
//	@ResponseBody
	public String list(Model model,@RequestParam(value = "page", defaultValue = "0") int page) {
//		return "<h1>question list</h1>";
//		return "question_list";
		Page<Question> paging = questionService.getList(page);
		model.addAttribute("paging",paging);
		return "board/question_list";
	}
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model,@PathVariable("id") Integer id, AnswerForm answerForm) {
		Question q =  questionService.detail(id);
		model.addAttribute("question",q);		
		return "board/question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(QuestionForm questionFrom) {
		return "board/question_form";
	}	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String create(@Valid QuestionForm questionFrom, BindingResult bindingResult,
			Principal principal) {
		if(bindingResult.hasErrors())
			return "board/question_form";
		// 질문을 저장
		SiteUser siteUser = userService.getUser(principal.getName());
		questionService.create(questionFrom.getSubject(), questionFrom.getContent(),siteUser);
		// 질문을 저장한 후에는 질문 목록으로 이동
		return "redirect:/question/list";
	}
	
	// 질문 수정
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, 
    		@PathVariable("id") Integer id, 
    		Principal principal) {
        Question question = this.questionService.detail(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "board/question_form";
    }
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "board/question_form";
        }
        Question question = this.questionService.detail(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
	
	// 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	 public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.detail(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
	
	
}
