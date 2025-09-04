package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.dto.response.project.SubcategoryDto;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import com.example.funding.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.funding.common.Utils.getPercentNow;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final SubcategoryMapper subcategoryMapper;
    private final TagMapper tagMapper;
    private final RewardMapper rewardMapper;
    private final NewsMapper newsMapper;

    private final PaymentMapper paymentMapper;
    private final BackingDetailMapper backingDetailMapper;
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
        // 1) 최근 24시간 결제
//        Date since = Date.from(Instant.now().minus(24, ChronoUnit.HOURS));
        Date since = Date.from(Instant.now().minus(8760, ChronoUnit.HOURS));
        List<Payment> pays = paymentMapper.findRecentSuccessful(since);
        if (pays.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(200, "최근 24시간 내 결제된 프로젝트가 없습니다."));

        // backingId -> 결제금액 합계
        Map<Long, Long> payByBacking = pays.stream()
                .collect(Collectors.groupingBy(
                        Payment::getBackingId,
                        Collectors.summingLong(Payment::getAmount)));

        // 2) 백킹 → 리워드
        List<Long> backingIds = new ArrayList<>(payByBacking.keySet());
        List<BackingDetail> bds = backingDetailMapper.findByBackingIds(backingIds);
        if (bds.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(200, "최근 24시간 내 결제된 프로젝트가 없습니다."));

        // 3) 리워드 → 프로젝트
        List<Long> rewardIds = bds.stream().map(BackingDetail::getRewardId).distinct().toList();
        Map<Long, Long> projectIdByRewardId = rewardMapper.findProjectIdsByRewardIds(rewardIds)
                .stream().collect(Collectors.toMap(Reward::getRewardId, Reward::getProjectId));

        // 4) 프로젝트별 24시간 증가액 집계
        Map<Long, Long> amount24hByProject = new HashMap<>();

        // 가정: 한 backingId의 리워드들은 모두 같은 projectId (다르면 균등 분배 등 정책 필요)
        Map<Long, List<Long>> rewardIdsByBacking = bds.stream()
                .collect(Collectors.groupingBy(BackingDetail::getBackingId,
                        Collectors.mapping(BackingDetail::getRewardId, Collectors.toList())));

        for (Map.Entry<Long, Long> e : payByBacking.entrySet()) {
            Long backingId = e.getKey();
            long payAmount = e.getValue();

            List<Long> rewardsOfBacking = rewardIdsByBacking.getOrDefault(backingId, Collections.emptyList());
            if (rewardsOfBacking.isEmpty()) continue;

            Long projectId = projectIdByRewardId.get(rewardsOfBacking.getFirst());
            if (projectId == null) continue;

            amount24hByProject.merge(projectId, payAmount, Long::sum);
        }

        if (amount24hByProject.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(200, "최근 24시간 내 결제된 프로젝트가 없습니다."));

        // 5) 프로젝트 기본정보 일괄 조회
        List<Long> projectIds = new ArrayList<>(amount24hByProject.keySet());
        List<Project> projects = projectMapper.findByIdsAndStatus(projectIds, "OPEN");

        // 상태/최근성 필터: 최근 30일 시작
//        Instant cutoff = Instant.now().minus(30, ChronoUnit.DAYS);
        Instant cutoff = Instant.now().minus(400, ChronoUnit.DAYS);
        projects.removeIf(p ->
               (p.getStartDate() != null && p.getStartDate().toInstant().isBefore(cutoff)));

        // creatorIds
        List<Long> creatorIds = projects.stream().map(Project::getCreatorId).distinct().toList();
        Map<Long, String> creatorNameById = creatorMapper.findByIds(creatorIds).stream()
                .collect(Collectors.toMap(Creator::getCreatorId, Creator::getCreatorName));

        // 6) 점수 계산 및 DTO 조립
        List<RecentTop10ProjectDto> ranked = projects.stream().map(p -> {
                    long amount24h = amount24hByProject.getOrDefault(p.getProjectId(), 0L);
                    int like = Optional.of(p.getLikeCnt()).orElse(0);
                    int view = Optional.of(p.getViewCnt()).orElse(0);

                    int percentNow = getPercentNow(p.getCurrAmount(), p.getGoalAmount());

                    double trendScore =
                            // 24h 증가/목표 비중(70%)
                            ((p.getGoalAmount() > 0) ? ((double) amount24h / p.getGoalAmount() * 100.0) : 0.0) * 0.7
                                    + (like / 50.0) * 0.2
                                    + (view / 500.0) * 0.1;

                    return RecentTop10ProjectDto.builder()
                            .projectId(p.getProjectId())
                            .title(p.getTitle())
                            .creatorName(creatorNameById.getOrDefault(p.getCreatorId(), "크리에이터"))
                            .thumbnail(p.getThumbnail())
                            .currAmount(p.getCurrAmount())
                            .endDate(p.getEndDate())
                            .percentNow(percentNow)
                            .trendScore(trendScore)
                            .build();
                }).sorted(Comparator.comparingDouble(RecentTop10ProjectDto::getTrendScore).reversed())
                .limit(10)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "최근 24시간 내 결제된 프로젝트 TOP10 조회 성공", ranked));
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
        List<Project> featured = projectMapper.findFeatured(days, limit);
        if (featured.size() > limit) {
            featured = featured.subList(0, limit);
        }
        // 부족하면 추가 조회
        if (featured.size() < limit) {
            List<Long> existingIds = featured.stream().map(Project::getProjectId).toList();
            List<Project> supplement = projectMapper.findFeaturedExcluding(limit - featured.size(), existingIds);
            featured.addAll(supplement);
        }
        if (featured.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "추천 프로젝트가 없습니다."));
        }
        List<Long> creatorIds = featured.stream().map(Project::getCreatorId).distinct().toList();
        Map<Long, String> creatorNameById = creatorMapper.findByIds(creatorIds).stream()
                .collect(Collectors.toMap(Creator::getCreatorId, Creator::getCreatorName));
        List<FeaturedProjectDto> result = featured.stream().map(p -> {
            int percentNow = getPercentNow(p.getCurrAmount(), p.getGoalAmount());
            double score = 0.0;
            if (p.getGoalAmount() > 0) {
                score = (percentNow * 0.5) + ((p.getBackerCnt() / 50.0) * 0.2) + ((p.getLikeCnt() / 20.0) * 0.2) + ((p.getViewCnt() / 500.0) * 0.1);
            }
            return FeaturedProjectDto.builder()
                    .projectId(p.getProjectId())
                    .title(p.getTitle())
                    .creatorName(creatorNameById.getOrDefault(p.getCreatorId(), "크리에이터"))
                    .thumbnail(p.getThumbnail())
                    .percentNow(percentNow)
                    .goalAmount(p.getGoalAmount())
                    .score(score)
                    .build();
        }).sorted(Comparator.comparingDouble(FeaturedProjectDto::getScore).reversed()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "추천 프로젝트 조회 성공", result));
    }
}
