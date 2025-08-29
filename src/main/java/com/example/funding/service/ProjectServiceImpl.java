package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final SubcategoryMapper subcategoryMapper;
    private final TagMapper tagMapper;
    private final RewardMapper rewardMapper;
    private final NewsMapper newsMapper;
    private final CommunityMapper communityMapper;

    @Override
    public ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId) {
        Project project = projectMapper.getProjectById(projectId);
        Subcategory subcategory = subcategoryMapper.getSubcategoryById(project.getSubctrgId());
        List<Tag> tagList = tagMapper.getTagListById(projectId);
        List<Reward> rewardList = rewardMapper.getRewardListById(projectId);
        List<News> newsList = newsMapper.getNewsListById(projectId);
        List<Community> communityList = communityMapper.getCommunityListById(projectId);

        ProjectDetailDto projectDetailDto = ProjectDetailDto.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .goalAmount(project.getGoalAmount())
                .currAmount(project.getCurrAmount())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .content(project.getContent())
                .thumbnail(project.getThumbnail())
                .projectStatus(project.getProjectStatus())
                .backerCnt(project.getBackerCnt())
                .viewCnt(project.getViewCnt())
                .creatorId(project.getCreatorId())
                .subctrgId(subcategory.getSubctrgId())
                .subctrgName(subcategory.getSubctrgName())
                .ctrgId(subcategory.getCtrgId())
                .ctrgName(subcategory.getSubctrgName())
                .tagList(tagList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "프로젝트 상세페이지 조회 성공", projectDetailDto));
    }
}
