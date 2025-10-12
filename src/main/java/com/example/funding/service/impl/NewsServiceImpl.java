package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.NewsCreateRequestDto;
import com.example.funding.mapper.NewsMapper;
import com.example.funding.model.News;
import com.example.funding.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
    private final NewsMapper newsMapper;

    @Override
    public ResponseEntity<ResponseDto<String>> createNews(Long projectId, Long creatorId, NewsCreateRequestDto dto) {
        //TODO: guard, validator

        News news = News.builder()
            .projectId(projectId)
            .content(dto.getContent())
            .build();

        int result = newsMapper.createNews(news);
        if (result != 1 || news.getNewsId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "새소식 등록 실패");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "새소식 등록 성공", null));
    }
}
