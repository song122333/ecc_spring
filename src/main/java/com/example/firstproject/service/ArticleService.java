package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    public List<Article> index(){
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article=dto.toEntity();
        if (article.getId()!=null){
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        //dto-> 엔티티
        Article article=dto.toEntity();
        log.info("id:{},article:{}",id,article.toString());
        //타깃 조회
        Article target=articleRepository.findById(id).orElse(null);
        //잘못된 요청 처리
        if (target==null || id != article.getId()){
            log.info("잘못된 요청! id:{},article:{}",id,article.toString());
            return null;
        }
        //업데이트 및 정상응답(200)
        target.patch(article);
        Article updated=articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        //대상 찾기
        Article target = articleRepository.findById(id).orElse(null);
        //잘못된 요청 처리
        if (target == null) {
            return null;
        }
        //대상 삭제
        articleRepository.delete(target);
        return target;
    }
}

