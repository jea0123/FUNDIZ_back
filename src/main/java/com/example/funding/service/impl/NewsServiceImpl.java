package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.NewsCreateRequestDto;
import com.example.funding.exception.forbidden.AccessDeniedException;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.NewsMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.model.News;
import com.example.funding.model.Project;
import com.example.funding.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
    private final NewsMapper newsMapper;
    private final CreatorMapper creatorMapper;
    private final ProjectMapper projectMapper;

    /**
     * <p>프로젝트 새소식 등록</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto       NewsCreateRequestDto
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> createNews(Long projectId, Long creatorId, NewsCreateRequestDto dto) {
        Project existingProject = projectMapper.findById(projectId);
        if (existingProject == null) throw new CreatorNotFoundException();
        if (creatorMapper.findById(creatorId) == null) throw new CreatorNotFoundException();
        if (!existingProject.getCreatorId().equals(creatorId)) throw new AccessDeniedException();

        //TODO: guard, validator

        News news = News.builder()
                .projectId(projectId)
                .content(dto.getContent())
                .build();

        newsMapper.createNews(news);
        return ResponseEntity.ok(ResponseDto.success(200, "새소식 등록 성공", null));
    }
}
