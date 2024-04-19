package com.green.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.green.dto.ArticleDto;
import com.green.entity.Article;
import com.green.repository.ArticleRepository;

@Controller
public class ArticleController {
	
	@Autowired
	private  ArticleRepository  articleRepository;
	
	// data 입력
	@GetMapping("/articles/WriteForm")
	public  String  writeForm() {
		return "articles/write"; 
		// /stc/main/resources/templates/   articles/write  .mustache
	}
	
	// data 저장
	// 405 에러 : method="post" -> @GetMapping 
	//  에러 : @GetMapping("/articles/Write") , DTO == VO
	// FormData - title : aaa content: 가가가
	@PostMapping("/articles/Write")
	public  String  write( ArticleDto articleDto ) {
		// 넘어온 데이터 확인
		System.out.println( "결과:" + articleDto.toString() );  // 책 : ArticleForm
		// db 에 저장 h2 article 테이블에 저장
		// Entity : db 의 테이블이다
		// 1.  Dto -> Entity 
		Article  article = articleDto.toEntity();
		// 2.  리포지터리(인터페이스)를 사용하여 엔티티을  저장
		Article  saved   = articleRepository.save( article );    // INSERT 
		System.out.println("saved:" + saved);
		
		return "redirect:/articles/List";
	}
	
	// 1번 데이터 조회 : PathVariable -> GET
	// java.lang.IllegalArgumentException: Name for argument of type
	// 1번 방법. @PathVariable(value="id") 추가
	// 2번 방법. sts 설정 추가 
	// 프로젝트 -> properties  -> Java Compiler -> 
	// -> ✅ project specific settings 체크
	// -> ✅ store infomation ..... 체크
	
	// No default constructor for entity 'com.green.entity.Article' 
	// Article 에 @NoArgsConstructor  추가
	// localhost:9090/articles/1
	@GetMapping("/articles/{id}")
	public  String  view(
			@PathVariable(value="id")  Long id,  
			Model   model) {
		// Article  articleEntity  = articleRepository.findById(id); 
		// 1번방법  // Type mismatch error
		// Optional<Article>  articleEntity  = articleRepository.findById(id);
		// 값이 있으면 Article 을 리턴, 값이 없으면 null 리턴
		
		// 2번 방법
		Article  articleEntity  = articleRepository.findById(id).orElse(null);
		System.out.println( "1번 조회 결과:" + articleEntity );
		model.addAttribute("article", articleEntity ); // 조회한 결과 -> model
		return "articles/view";  // articles/view.mustache
	}
	
	@GetMapping("/articles/List")
	public  String   list(Model model) {
		// List<Article> articleEntityList =  articleRepository.findAll();
		// 1. 오류처리 1번 방법
		// List<Article> articleEntityList 
		//     =  (List<Article>) articleRepository.findAll();				
		// 2. ArticleRepository interface 에 함수를 등록
		
		List<Article> articleEntityList =  articleRepository.findAll();
		System.out.println( "전체목록:" + articleEntityList );
		model.addAttribute("articleList",  articleEntityList);		
		
		return  "articles/list";
		
	}

	// 데이터 수정페이지로 이동
	@GetMapping("/articles/{id}/EditForm")
	public  String  editForm(
		@PathVariable(value="id") Long id) {
	
		return  "articles/edit";
	}
	
	// 데이터 수정
	@GetMapping("/articles/{id}/Edit")
	public  String  edit() {
	  
		// db 수정
		
	//	return  "redirect:/articles/view/" + id;
		return  "redirect:/articles/List/";
	}
}

















