package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.NewsCreateRequestDto;
import org.springframework.http.ResponseEntity;

public interface NewsService {
    ResponseEntity<ResponseDto<Long>> createNews(Long projectId, Long creatorId, NewsCreateRequestDto dto);
}
