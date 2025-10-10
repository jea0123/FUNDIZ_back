package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.SearchProjectDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.dto.row.ProjectRow;
import com.example.funding.mapper.NewsMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.mapper.TagMapper;
import com.example.funding.model.News;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import com.example.funding.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final TagMapper tagMapper;
    private final RewardMapper rewardMapper;
    private final NewsMapper newsMapper;

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
        //조회수 증가
        projectMapper.updateViewCnt(projectId);

        //프로젝트 단건 조회
        ProjectRow row = projectMapper.getProjectRow(projectId);
        if (row == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "존재하지 않는 프로젝트입니다."));
        }

        //컬렉션 조회
        List<Tag> tagList = tagMapper.getTagList(projectId);
        List<Reward> rewardList = rewardMapper.getRewardList(projectId);
        List<News> newsList = newsMapper.getNewsList(projectId);

        //달성률, 창작자가 등록한 프로젝트 수, 결제일
        int percentNow = Utils.getPercentNow(row.getCurrAmount(), row.getGoalAmount());
        int projectCnt = projectMapper.getProjectCnt(row.getCreatorId());
        LocalDate paymentDate = row.getEndDate().plusDays(1);

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
        LocalDate since = LocalDate.now().minusDays(800);
        int startDays = 8760;  // 프로젝트 시작일 최근 30일
        int limit = 10;

        List<RecentTop10ProjectDto> ranked = projectMapper.findRecentTopProjectsJoined(since, startDays, limit);

        if (ranked == null || ranked.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "최근 24시간 내 결제된 프로젝트 TOP10이 없습니다.");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "추천 프로젝트가 없습니다.");
        }
        return ResponseEntity.ok(ResponseDto.success(200, "추천 프로젝트 조회 성공", result));
    }

    /**
     * <p>검색 기능 (제목, 내용, 창작자명, 태그)</p>
     *
     * @param dto SearchProjectDto
     * @param pager pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> searchProject(SearchProjectDto dto, Pager pager) {
        int total = projectMapper.countSearchProjects(dto);

        List<FeaturedProjectDto> items = Collections.emptyList();
        if (total > 0) {
            items = projectMapper.searchProjects(dto, pager);
        }
        PageResult<FeaturedProjectDto> result = PageResult.of(items, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 검색 성공", result));
    }

}
