package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.NewsCreateRequestDto;
import com.example.funding.exception.badrequest.ContentRequiredException;
import com.example.funding.mapper.NewsMapper;
import com.example.funding.model.News;
import com.example.funding.model.Project;
import com.example.funding.service.NewsService;
import com.example.funding.validator.Loaders;
import com.example.funding.validator.PermissionChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.example.funding.validator.Preconditions.requireHasText;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
    private final Loaders loaders;
    private final PermissionChecker auth;
    private final NewsMapper newsMapper;

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
        Project existing = loaders.project(projectId);
        if (!existing.getProjectStatus().equals("OPEN"))
            throw new IllegalArgumentException("진행 중인 프로젝트에만 새소식을 등록할 수 있습니다.");
        loaders.creator(creatorId);
        auth.mustBeOwner(creatorId, existing.getCreatorId());
        requireHasText(dto.getContent(), ContentRequiredException::new);

        //TODO: guard, validator
        News news = News.builder()
                .projectId(projectId)
                .content(dto.getContent())
                .build();

        newsMapper.createNews(news);
        return ResponseEntity.ok(ResponseDto.success(200, "새소식 등록 성공", null));
    }
}
