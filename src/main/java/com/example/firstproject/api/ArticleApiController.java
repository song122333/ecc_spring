package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@Slf4j
public class ArticleApiController {
    @Autowired
    private ArticleRepository articleRepository;
    //GET
    @GetMapping("/api/articles")
    public ArrayList<Article> index(){
        return articleRepository.findAll();
    }
    @GetMapping("/api/articles'{id}")
    public Article show(@PathVariable Long id){
        return articleRepository.findById(id).orElse(null);
    }
    //POST
    @PostMapping("/api/articles")
    public Article create(@RequestBody ArticleForm dto){
        Article article=dto.toEntity();
        return articleRepository.save(article);
    }
    //PATCH
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody ArticleForm dto){
        //dto-> 엔티티
        Article article=dto.toEntity();
        log.info("id:{},article:{}",id,article.toString());
        //타깃 조회
        Article target=articleRepository.findById(id).orElse(null);
        //잘못된 요청 처리
        if (target==null || id!=article.getId()){
            log.info("잘못된 요청! id:{},article:{}",id,article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //업데이트 및 정상응답(200)
        target.patch(article);
        Article updated=articleRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
    //DELETE
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id){
        //대상 찾기
        Article target=articleRepository.findById(id).orElse(null);
        //잘못된 요청 처리
        if(target==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //대상 삭제
        articleRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
