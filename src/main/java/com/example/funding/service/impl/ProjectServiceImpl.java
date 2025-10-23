package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.SearchProjectDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectCountsDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.dto.row.CountsAgg;
import com.example.funding.dto.row.ProjectRow;
import com.example.funding.exception.badrequest.InvalidSortException;
import com.example.funding.exception.notfound.ProjectNotFoundException;
import com.example.funding.mapper.*;
import com.example.funding.model.News;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import com.example.funding.service.ProjectService;
import com.example.funding.validator.Loaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.example.funding.validator.Preconditions.requireIn;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final Loaders loaders;
    private final ProjectMapper projectMapper;
    private final TagMapper tagMapper;
    private final RewardMapper rewardMapper;
    private final NewsMapper newsMapper;
    private final CommunityMapper communityMapper;

    /**
     * <p>프로젝트 상세 페이지 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 조은애
     * @since 2025-08-31
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId) {
        ProjectRow row = projectMapper.getProjectRow(projectId);
        if (row == null) throw new ProjectNotFoundException();
        //조회수 증가
        projectMapper.updateViewCnt(projectId);

        //컬렉션 조회
        List<Tag> tagList = tagMapper.getTagList(projectId);
        List<Reward> rewardList = rewardMapper.getRewardListPublic(projectId);
        List<News> newsList = newsMapper.getNewsList(projectId);

        //달성률, 창작자가 등록한 프로젝트 수, 결제일
        int percentNow = Utils.getPercentNow(row.getCurrAmount(), row.getGoalAmount());
        int projectCnt = projectMapper.getProjectCnt(row.getCreatorId());
        LocalDateTime paymentDate = row.getEndDate().plusDays(1);

        //응답Dto 조립
        ProjectDetailDto detail = ProjectDetailDto.builder()
                .projectId(row.getProjectId())
                .creatorId(row.getCreatorId())
                .subctgrId(row.getSubctgrId())
                .title(row.getTitle())
                .goalAmount(row.getGoalAmount())
                .currAmount(row.getCurrAmount())
                .startDate(row.getStartDate())
                .endDate(row.getEndDate())
                .content(row.getContent())
                .contentBlocks(row.getContentBlocks())
                .thumbnail(row.getThumbnail())
                .projectStatus(row.getProjectStatus())
                .backerCnt(row.getBackerCnt())
                .likeCnt(row.getLikeCnt())
                .viewCnt(row.getViewCnt())
                .percentNow(percentNow)
                .projectCnt(projectCnt)
                .paymentDate(paymentDate)
                .creatorName(row.getCreatorName())
                .followerCnt(row.getFollowerCnt())
                .profileImg(row.getProfileImg())
                .ctgrName(row.getCtgrName())
                .subctgrName(row.getSubctgrName())
                .tagList(tagList)
                .rewardList(rewardList)
                .newsList(newsList)
                .build();

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 상세 조회 성공", detail));
    }

    /**
     * 최근 24시간 내 결제된 프로젝트 TOP10 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<RecentTop10ProjectDto>>> getRecentTop10() {
//        Date since = Date.from(Instant.now().minus(24, ChronoUnit.HOURS));
        LocalDate since = LocalDate.now().minusDays(800);
        int startDays = 8760;  // 프로젝트 시작일 최근 30일
        int limit = 8;

        List<RecentTop10ProjectDto> ranked = projectMapper.findRecentTopProjectsJoined(since, startDays, limit);

        if (ranked == null || ranked.isEmpty()) {
            return ResponseEntity.ok(ResponseDto.success(200, "최근 24시간 내 결제된 프로젝트 TOP10 조회 성공", Collections.emptyList()));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "최근 24시간 내 결제된 프로젝트 TOP10 조회 성공", ranked));
    }

    /**
     * 추천 프로젝트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<FeaturedProjectDto>>> getFeatured(int days, int limit) {
        List<FeaturedProjectDto> result = projectMapper.findFeaturedJoinedWithRecent(days, limit);
        if (result == null || result.isEmpty()) {
            return ResponseEntity.ok(ResponseDto.success(200, "추천 프로젝트 조회 성공", Collections.emptyList()));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "추천 프로젝트 조회 성공", result));
    }

    /**
     * <p>검색 기능 (제목, 내용, 창작자명, 태그)</p>
     *
     * @param dto   SearchProjectDto
     * @param pager pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> searchProject(SearchProjectDto dto, Pager pager) {
        requireIn(dto.getSort(), List.of("recent", "liked", "amount", "deadline", "percent", "view"), InvalidSortException::new);
        int total = projectMapper.countSearchProjects(dto);

        List<FeaturedProjectDto> items = Collections.emptyList();
        if (total > 0) {
            items = projectMapper.searchProjects(dto, pager);
        }
        PageResult<FeaturedProjectDto> result = PageResult.of(items, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 검색 성공", result));
    }

    /**
     * 프로젝트 좋아요 수 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<Long>> getLikeCnt(Long projectId) {
        loaders.project(projectId);
        Long likeCnt = projectMapper.getLikeCnt(projectId);
        return ResponseEntity.ok(ResponseDto.success(200, "좋아요 수 조회 성공", likeCnt));
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티,후기 수 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-20
     */
    @Override
    public ResponseEntity<ResponseDto<ProjectCountsDto>> getCounts(Long projectId) {
        CountsAgg agg = communityMapper.countByProjectGrouped(projectId);

        long cm = (agg != null && agg.getCommunityTotal() != null) ? agg.getCommunityTotal() : 0L;
        long rv = (agg != null && agg.getReviewTotal() != null) ? agg.getReviewTotal() : 0L;

        ProjectCountsDto dto = new ProjectCountsDto();
        dto.setCommunity(ProjectCountsDto.Section.builder().total(cm).build());
        dto.setReview(ProjectCountsDto.Section.builder().total(rv).build());

        return ResponseEntity.ok(ResponseDto.success(200, "커뮤니티/후기 수 조회 성공", dto));
    }
}
