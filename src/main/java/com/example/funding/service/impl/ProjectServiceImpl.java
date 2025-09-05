package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.dto.response.project.SubcategoryDto;
import com.example.funding.mapper.*;
import com.example.funding.model.News;
import com.example.funding.model.Project;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import com.example.funding.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final SubcategoryMapper subcategoryMapper;
    private final TagMapper tagMapper;
    private final RewardMapper rewardMapper;
    private final NewsMapper newsMapper;

    private final CreatorMapper creatorMapper;

    /**
     * <p>프로젝트 상세 페이지 조회</p>
     * <p>조회수 +1</p>
     *
     * @param projectId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-08-31
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId) {
        //조회수 증가
        projectMapper.updateViewCnt(projectId);

        Project project = projectMapper.getProjectById(projectId);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "프로젝트를 찾을 수 없습니다."));
        }

        SubcategoryDto subcategory = subcategoryMapper.getSubcategoryById(project.getSubctgrId());

        List<Tag> tagList = tagMapper.getTagListById(projectId);
        List<Reward> rewardList = rewardMapper.getRewardListById(projectId);
        List<News> newsList = newsMapper.getNewsListById(projectId);

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
                .subcategory(subcategory)
                .tagList(tagList)
                .rewardList(rewardList)
                .newsList(newsList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "프로젝트 상세 조회 성공", projectDetailDto));
    }

    /**
     * <p>최근 24시간 내 결제된 프로젝트 TOP10 조회</p>
     * <p>트렌드 점수 산정 기준 (가중치)</p>
     * <ul>
     *     <li>최근 24시간 결제금액/목표금액 비중: 70%</li>
     *     <li>좋아요 수: 20%</li>
     *     <li>조회수: 10%</li>
     * </ul>
     * <p>조건</p>
     * <ul>
     *     <li>프로젝트 상태: OPEN</li>
     *     <li>프로젝트 시작일: 최근 30일 이내</li>
     * </ul>
     *
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 장민규
     * @since 2025-09-03
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<RecentTop10ProjectDto>>> getRecentTop10() {
//        Date since = Date.from(Instant.now().minus(24, ChronoUnit.HOURS));
        Date since = Date.from(Instant.now().minus(8760, ChronoUnit.HOURS));
        int startDays = 30;  // 프로젝트 시작일 최근 30일
        int limit = 10;

        List<RecentTop10ProjectDto> ranked = projectMapper.findRecentTopProjectsJoined(since, startDays, limit);

        if (ranked == null || ranked.isEmpty()) {
            return ResponseEntity.ok(ResponseDto.fail(200, "최근 24시간 내 결제된 프로젝트가 없습니다."));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "최근 24시간 내 결제된 프로젝트 TOP10 조회 성공", ranked));
    }

    /**
     * <p>추천 프로젝트 조회</p>
     * <p>추천 알고리즘 점수 산정 기준 (가중치)</p>
     * <ul>
     *     <li>달성률: 50%</li>
     *     <li>후원자 수: 20%</li>
     *     <li>좋아요 수: 20%</li>
     *     <li>조회수: 10%</li>
     * </ul>
     * <p>조건</p>
     * <ul>
     *     <li>프로젝트 상태: OPEN</li>
     *     <li>프로젝트 시작일: 최근 N일 이내</li>
     * </ul>
     *
     * @param days  최근 N일 이내 시작한 프로젝트
     * @param limit 최대 조회 개수
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 장민규
     * @since 2025-09-04
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<FeaturedProjectDto>>> getFeatured(int days, int limit) {
        List<FeaturedProjectDto> result = projectMapper.findFeaturedJoinedWithRecent(days, limit);
        if (result == null || result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.fail(404, "추천 프로젝트가 없습니다."));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "추천 프로젝트 조회 성공", result));
    }
}
