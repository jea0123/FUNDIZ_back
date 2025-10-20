package com.example.funding.service.impl;

import com.example.funding.common.*;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.CreatorRegisterRequestDto;
import com.example.funding.dto.request.creator.CreatorUpdateRequestDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.request.shipping.ShippingStatusDto;
import com.example.funding.dto.response.backing.BackingCreatorBackerList;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import com.example.funding.enums.ProjectStatus;
import com.example.funding.enums.ShippingStatus;
import com.example.funding.exception.badrequest.AlreadyCreatorException;
import com.example.funding.exception.badrequest.InvalidParamException;
import com.example.funding.exception.badrequest.InvalidStatusException;
import com.example.funding.mapper.*;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import com.example.funding.service.CreatorService;
import com.example.funding.service.RewardService;
import com.example.funding.service.validator.ProjectInputValidator;
import com.example.funding.service.validator.ProjectTransitionGuard;
import com.example.funding.service.validator.ProjectValidationRules;
import com.example.funding.service.validator.ShippingValidator;
import com.example.funding.validator.Loaders;
import com.example.funding.validator.PermissionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.funding.validator.Preconditions.requireIn;
import static com.example.funding.validator.Preconditions.requireInEnum;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreatorServiceImpl implements CreatorService {
    private final CreatorMapper creatorMapper;
    private final ProjectMapper projectMapper;
    private final TagMapper tagMapper;
    private final RewardService rewardService;
    private final RewardMapper rewardMapper;
    private final UserMapper userMapper;
    private final SettlementMapper settlementMapper;
    private final BackingMapper backingMapper;
    private final ShippingMapper shippingMapper;
    private final FollowMapper followMapper;
    private final CommunityMapper communityMapper;
    private final ProjectInputValidator inputValidator;
    private final ProjectTransitionGuard transitionGuard;
    private final FileUploader fileUploader;
    private final Loaders loaders;
    private final PermissionChecker auth;
    private final ShippingValidator shippingValidator;

    private static List<String> normalizeTags(List<String> tagList) {
        //불변 빈 리스트
        if (tagList == null) return List.of();

        List<String> out = new ArrayList<>(tagList.size());
        for (String tag : tagList) {
            if (tag == null) continue;
            String display = ProjectValidationRules.normTagDisplay(tag);
            if (!display.isEmpty()) out.add(display);
        }

        return List.copyOf(out);
    }

    /**
     * <p>프로젝트 목록 조회</p>
     *
     * @param creatorId 창작자 ID
     * @param dto       SearchCreatorProjectDto
     * @param pager     pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-05
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<CreatorProjectListDto>>> getProjectList(Long creatorId, SearchCreatorProjectDto dto, Pager pager) {
        loaders.creator(creatorId);
        requireIn(dto.getRangeType(), List.of("7d", "30d", "90d"), InvalidParamException::new);
//        requireInEnum(dto.getProjectStatus(), ProjectStatus.class, InvalidStatusException::new, "", "all", "ALL");
        List<ProjectStatus> st = dto.getProjectStatus();
        if (st != null && st.stream().anyMatch(Objects::isNull)) {
            throw new InvalidStatusException();
        }

        dto.applyRangeType();

        int total = creatorMapper.countProject(creatorId, dto);
        List<CreatorProjectListDto> items = Collections.emptyList();
        if (total > 0) {
            items = creatorMapper.getProjectList(creatorId, dto, pager);
            for (CreatorProjectListDto item : items) {
                item.setPercentNow(Utils.getPercentNow(item.getCurrAmount(), item.getGoalAmount()));
            }
        }

        PageResult<CreatorProjectListDto> result = PageResult.of(items, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 목록 조회 성공", result));
    }

    /**
     * <p>프로젝트 상세 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-05
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CreatorProjectDetailDto>> getProjectDetail(Long projectId, Long creatorId) {
        loaders.creator(creatorId);
        loaders.project(projectId);
        // Guard: 소유자
        transitionGuard.ensureProjectOwner(projectId, creatorId);

        CreatorProjectDetailDto dto = creatorMapper.getProjectDetail(projectId, creatorId);
        dto.setTagList(tagMapper.getTagList(projectId));
        dto.setRewardList(rewardMapper.getRewardListPublic(projectId)); //TODO: getProjectRewardsManage

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 상세 조회 성공", dto));
    }

    /**
     * <p>프로젝트 생성</p>
     *
     * @param dto       ProjectCreateRequestDto
     * @param creatorId 사용자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-09
     */
    @Override
    public ResponseEntity<ResponseDto<Long>> createProject(ProjectCreateRequestDto dto, Long creatorId) {
        loaders.creator(creatorId);
        // Guard
//        transitionGuard.assertCanCreate(creatorId);

        // Validator
//        List<String> errors = inputValidator.validateProjectCreate(dto);
//        if (!errors.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
//        }

        // 프로젝트 생성
        Project project = Project.builder()
                .creatorId(creatorId)
                .subctgrId(dto.getSubctgrId())
                .title(dto.getTitle())
                .goalAmount(dto.getGoalAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .content(dto.getContent())
                .contentBlocks(dto.getContentBlocks())
                .thumbnail(dto.getThumbnailUrl())
                .businessDoc(dto.getBusinessDocUrl())
                .build();

        int result = creatorMapper.saveProject(project);
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 생성 실패");
        }

        Long newId = project.getProjectId();

        // 태그 저장
        List<String> normalized = normalizeTags(dto.getTagList());
        for (String tagName : normalized) {
            tagMapper.saveTag(newId, tagName);
        }

        // 리워드 생성
        if (dto.getRewardList() != null && !dto.getRewardList().isEmpty()) {
            rewardService.createReward(newId, dto.getRewardList(), dto.getEndDate(), true);
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 생성 성공", newId));
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param dto       ProjectCreateRequestDto
     * @param creatorId 사용자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateProject(ProjectCreateRequestDto dto, Long creatorId) {
        loaders.creator(creatorId);
        Project existing = loaders.project(dto.getProjectId());
        auth.mustBeOwner(creatorId, existing.getCreatorId());
//        Project existing = projectMapper.findById(dto.getProjectId());
//        if (existing == null) throw new ProjectNotFoundException();
//        if (creatorMapper.findById(creatorId) == null) throw new CreatorNotFoundException();
//        if (!existing.getCreatorId().equals(creatorId)) throw new AccessDeniedException();
//
//        // Guard
//        transitionGuard.assertCanUpdate(dto.getProjectId(), creatorId);
//        // Validator
//        List<String> errors = inputValidator.validateProjectUpdate(dto);
//        if (!errors.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
//        }

        Project project = Project.builder()
                .creatorId(creatorId)
                .projectId(dto.getProjectId())
                .subctgrId(dto.getSubctgrId())
                .title(dto.getTitle())
                .goalAmount(dto.getGoalAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .content(dto.getContent())
                .contentBlocks(dto.getContentBlocks())
                .thumbnail(dto.getThumbnailUrl())
                .businessDoc(dto.getBusinessDocUrl())
                .build();

        creatorMapper.updateProject(project);

        // 태그 전체 삭제 후 저장
        tagMapper.deleteTags(dto.getProjectId());
        List<String> normalized = normalizeTags(dto.getTagList());
        for (String tagName : normalized) {
            tagMapper.saveTag(dto.getProjectId(), tagName);
        }

        //리워드 전체 삭제 후 저장
        rewardService.replaceRewards(dto.getProjectId(), dto.getRewardList(), dto.getEndDate());

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 수정 성공", null));
    }

    /**
     * <p>프로젝트 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteProject(Long projectId, Long creatorId) {
        loaders.creator(creatorId);
        Project existing = loaders.project(projectId);
        auth.mustBeOwner(creatorId, existing.getCreatorId());
//        Project existing = projectMapper.findById(projectId);
//        if (existing == null) throw new ProjectNotFoundException();
//        if (creatorMapper.findById(creatorId) == null) throw new CreatorNotFoundException();
//        if (!existing.getCreatorId().equals(creatorId)) throw new AccessDeniedException();
//        // Guard
//        transitionGuard.assertCanDelete(projectId, creatorId);

        rewardMapper.deleteRewards(projectId);
        tagMapper.deleteTags(projectId);

        creatorMapper.deleteProject(projectId);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 삭제 성공", null));
    }

    /**
     * <p>프로젝트 심사 요청</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-18
     */
    @Override
    public ResponseEntity<ResponseDto<String>> verifyProject(Long projectId, Long creatorId) {
        Project existing = loaders.project(projectId);
        loaders.creator(creatorId);
        auth.mustBeOwner(creatorId, existing.getCreatorId());
        // Guard
        transitionGuard.assertCanSubmit(projectId, creatorId);

        //상태 업데이트
        creatorMapper.markVerifyProject(projectId);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 심사 요청 성공", null));
    }

    /**
     * <p>프로젝트 요약</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-08
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CreatorProjectSummaryDto>> getProjectSummary(Long projectId, Long creatorId) {
        Project project = loaders.project(projectId);
        loaders.creator(creatorId);
        auth.mustBeOwner(creatorId, project.getCreatorId());

        CreatorProjectSummaryDto dto = new CreatorProjectSummaryDto(
                project.getProjectId(),
                project.getTitle(),
                project.getEndDate(),
                project.getProjectStatus()
        );

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 요약 조회 성공", dto));
    }

    /**
     * <p>창작자 프로필 조회</p>
     *
     * <li>창작자명</li>
     * <li>사업자번호</li>
     * <li>이메일</li>
     * <li>전화번호</li>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-11
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CreatorProfileSummaryDto>> getCreatorProfileSummary(Long creatorId) {
        loaders.creator(creatorId);
        // Guard
        transitionGuard.ensureCreatorExistsOrThrow(creatorId);

        CreatorProfileSummaryDto summary = creatorMapper.getCreatorProfileSummary(creatorId);

        return ResponseEntity.ok(ResponseDto.success(200, "창작자 프로필 조회 성공", summary));
    }

    /**
     * <p>QnA 내역 목록 조회(창작자 기준)</p>
     *
     * @param creatorId 창작자 ID
     * @param pager     Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-08
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfCreator(Long creatorId, Pager pager) {
        loaders.creator(creatorId);
        int total = creatorMapper.qnaTotalOfCreator(creatorId);

        List<CreatorQnaDto> qnaList = creatorMapper.getQnaListOfCreator(creatorId, pager);

        PageResult<CreatorQnaDto> result = PageResult.of(qnaList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "Q&A 목록 조회 성공", result));
    }

    /**
     * <p>프로젝트 목록 조회</p>
     *
     * @param creatorId 창작자 ID
     *                  대시보드
     * @return 성공 시 200 OK
     * @author 이윤기
     * @since 2025-10-08
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CreatorDashboardDto>> getCreatorDashBoard(Long creatorId) {
        loaders.creator(creatorId);

        int projectTotal = projectMapper.getProjectCnt(creatorId);
        Long totalAmount = settlementMapper.getTotalAmountCreatorId(creatorId);
        Long totalBackingCnt = backingMapper.getBackerCnt(creatorId);
        Long totalVerifyingCnt = projectMapper.getVerifyingCnt(creatorId);

        CreatorDashboardDto dashboardPie = creatorMapper.creatorDashboardDto(creatorId);
        double totalProjectCnt = dashboardPie.getTotalProjectCnt();
        double failedProjectCnt = dashboardPie.getProjectFailedCnt();

        //달성률 계산 (실패)
        double failedProject = (failedProjectCnt / totalProjectCnt) * 100;
        failedProject = Math.round(failedProject * 100.0) / 100.0;

        //달성률 계산(성공)
        double successProject = 100.0 - failedProject;

        List<CreatorDashboardRankDto> DashboardRank = creatorMapper.getProjectRankDate(creatorId);

        //내 프로젝트 후원자 랭킹
        List<CreatorDashboardRankDto> top3Backer = DashboardRank.stream()
                .sorted(Comparator.comparingLong(CreatorDashboardRankDto::getBackerCnt)
                        .reversed()).limit(3).toList();

        //내 프로젝트 좋아요 랭킹
        List<CreatorDashboardRankDto> top3Like = DashboardRank.stream()
                .sorted(Comparator.comparingLong(CreatorDashboardRankDto::getLikeCnt)
                        .reversed()).limit(3).toList();

        //내 프로젝트 조회수 랭킹
        List<CreatorDashboardRankDto> top3View = DashboardRank.stream()
                .sorted(Comparator.comparingLong(CreatorDashboardRankDto::getViewCnt)
                        .reversed()).limit(3).toList();

        List<DailyCountDto> dailyCount = backingMapper.dailyCount(creatorId);
        List<MonthCountDto> monthCount = backingMapper.monthCount(creatorId);

        CreatorDashboardDto result = CreatorDashboardDto.builder()
                .creatorId(creatorId)
                .projectTotal(projectTotal)
                .totalAmount(totalAmount)
                .totalBackingCnt(totalBackingCnt)
                .totalVerifyingCnt(totalVerifyingCnt)
                .totalProjectCnt(totalProjectCnt)
                .projectFailedCnt(failedProjectCnt)
                .projectFailedPercentage(failedProject)
                .projectSuccessPercentage(successProject)
                .top3BackerCnt(top3Backer)
                .top3LikeCnt(top3Like)
                .top3ViewCnt(top3View)
                .dailyStatus(dailyCount)
                .monthStatus(monthCount)
                .build();

        return ResponseEntity.ok(ResponseDto.success(200, "대시보드 불러오기 성공", result));
    }

    /**
     * <p>창작자의 후원 리스트</p>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author 이윤기
     * @since 2025-10-09
     */
    // 컨트롤러 하나에서 구현
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<BackingCreatorProjectListDto>>> getCreatorProjectList(Long creatorId) {
        loaders.creator(creatorId);

        //창작자의 모든 프로젝트 리스트
        List<BackingCreatorProjectListDto> backingCProjectList = projectMapper.getBackingCreatorProjectList(creatorId);

        //창작자의 모든 후원자 리스트
        List<BackingCreatorBackerList> backingCBackerList = backingMapper.getCBackerList(creatorId);

        //프로젝트별로 후원자 + 달성률
        List<BackingCreatorProjectListDto> result = backingCProjectList.stream().map(project -> {

            // 프로젝트별 달성률 계산
            double completionRate = 0;
            if (project.getGoalAmount() != null && project.getGoalAmount() > 0) {
                completionRate = ((double) project.getCurrAmount() / project.getGoalAmount()) * 100;
                completionRate = Math.round(completionRate * 100.0) / 100.0;
            }

            // 프로젝트별 후원자 리스트
            List<BackingCreatorBackerList> projectBackers = backingCBackerList.stream()
                    .filter(backer -> backer.getProjectId().equals(project.getProjectId()))
                    .toList();

            return BackingCreatorProjectListDto.builder()
                    .projectId(project.getProjectId())
                    .creatorId(project.getCreatorId())
                    .title(project.getTitle())
                    .goalAmount(project.getGoalAmount())
                    .currAmount(project.getCurrAmount())
                    .thumbnail(project.getThumbnail())
                    .backerCnt(project.getBackerCnt())
                    .backerList(projectBackers)
                    .completionRate(completionRate)
                    .build();
        }).toList();

        return ResponseEntity.ok(ResponseDto.success(200, "창작자의 프로젝트 후원 리스트 조회 성공", result));
    }

    /**
     * <p>창작자의 배송 (프로젝트)리스트</p>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author 이윤기
     * @since 2025-10-10
     */

    // 컨트롤러 나눠서 구현 -1
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<CreatorShippingProjectList>>> getCreatorShippingList(Long creatorId) {
        loaders.creator(creatorId);

        List<CreatorShippingProjectList> shippingProjectLists = projectMapper.getCShippingList(creatorId);

        return ResponseEntity.ok(ResponseDto.success(200, "창작자의 배송(프로젝트) 리스트 조회 성공", shippingProjectLists));
    }

    /**
     * <p>창작자의 배송 (후원자)리스트</p>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author 이윤기
     * @since 2025-10-10
     */
    // 컨트롤러 나눠서 구현 - 2
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<CreatorShippingBackerList>>> getShippingBackerList(Long creatorId, Long projectId) {
        loaders.creator(creatorId);
        Project project = loaders.project(projectId);
        auth.mustBeOwner(creatorId, project.getCreatorId());

        List<CreatorShippingBackerList> shippingBackerLists = shippingMapper.creatorShippingBackerList(creatorId, projectId);

        return ResponseEntity.ok(ResponseDto.success(200, "창작자의 배송(후원자) 리스트 조회 성공", shippingBackerLists));
    }

    /**
     * 크리에이터 등록
     */
    @Override
    public ResponseEntity<ResponseDto<String>> registerCreator(CreatorRegisterRequestDto dto, Long userId) throws IOException {
        loaders.user(userId);
        if (userMapper.getCreatorIdByUserId(userId) != null) throw new AlreadyCreatorException();

        Creator creator = Creator.builder()
                .userId(userId)
                .creatorName(dto.getCreatorName())
                .creatorType(dto.getCreatorType().name())
                .businessNum(dto.getBusinessNumber())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .account(dto.getAccount())
                .bank(dto.getBank())
                .build();
        if (dto.getProfileImg() != null && !dto.getProfileImg().isEmpty()) {
            String profileImgPath = fileUploader.upload(dto.getProfileImg());
            creator.setProfileImg(profileImgPath);
        }

        creatorMapper.insertCreator(creator);
        return ResponseEntity.ok(ResponseDto.success(200, "창작자 등록 성공", dto.getCreatorName()));
    }


    /**
     * <p>크리에이터 상세 정보 조회</p>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-15
     */
    @Override
    public ResponseEntity<ResponseDto<Creator>> item(Long creatorId) {
        loaders.creator(creatorId);
        Creator item = creatorMapper.creatorInfo(creatorId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "창작자 정보 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "창작자 정보 조회 성공", item));
    }

    /**
     * <p>크리에이터 정보 수정</p>
     *
     * @param creatorId 창작자 ID
     * @param dto       CreatorUpdateRequestDto
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-15
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateCreatorInfo(Long creatorId, CreatorUpdateRequestDto dto) {
        loaders.creator(creatorId);
        int result = creatorMapper.updateCreatorInfo(dto);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "창작자 정보 수정 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "창작자 정보 수정 완료", "창작자 정보 수정 "));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> setShippingStatus(Long projectId, Long creatorId, ShippingStatusDto shippingStatusDto) {
        if ("SHIPPED".equalsIgnoreCase(shippingStatusDto.getShippingStatus())) {
            if (shippingStatusDto.getTrackingNum() == null || !shippingStatusDto.getTrackingNum().matches("^[0-9]{10,14}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "배송 시작(SHIPPED) 상태로 변경할 때는 운송장 번호를 반드시 입력해야 합니다.");
            }
        }
        // 기존 상태 가져오기
        //Shipping current = shippingMapper.findByBackingId(shippingStatusDto.getBackingId());
        //String currentStatus = current.getShippingStatus();
        //검증
        //shippingValidator.validateTransition(currentStatus, shippingStatusDto);

        loaders.creator(creatorId);
        Project project = loaders.project(projectId);
        auth.mustBeOwner(creatorId, project.getCreatorId());
        requireInEnum(shippingStatusDto.getShippingStatus(), ShippingStatus.class, InvalidStatusException::new);

        int result = shippingMapper.updateShippingStatus(projectId, creatorId, shippingStatusDto);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "배송상태변경실패"));
        }

        return ResponseEntity.ok(ResponseDto.success(200, "배송상태 변경 완료", "배송상태 변경"));
    }

    /**
     * 팔로워 수 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<Long>> getFollowerCnt(Long creatorId) {
        loaders.creator(creatorId);
        Long followerCnt = followMapper.getFollowerCnt(creatorId);
        return ResponseEntity.ok(ResponseDto.success(200, "팔로워 수 조회 성공", followerCnt));
    }

    /**
     * 크리에이터 요약 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CreatorSummaryDto>> getCreatorSummary(Long creatorId, Long userId) {
        Creator existing = loaders.creator(creatorId);
        CreatorSummaryDto summary = CreatorSummaryDto.builder()
                .creator(creatorMapper.getCreatorRowById(creatorId))
                .stats(creatorMapper.getCreatorStatsById(creatorId))
                .followerCount(followMapper.getFollowerCnt(creatorId))
                .lastLogin(userMapper.getLastLoginTime(existing.getUserId()))
                .build();
        if (userId != null) {
            int isFollowing = followMapper.isFollowingCreator(userId, creatorId);
            summary.setIsFollowed(isFollowing > 0);
        } else {
            summary.setIsFollowed(false);
        }
        return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 요약 정보 조회 성공", summary));
    }

    /**
     * 크리에이터 프로젝트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<CreatorProjectDto>>> getCreatorProject(Long creatorId, String sort, Pager pager) {
        loaders.creator(creatorId);
        List<CreatorProjectDto> dtos = creatorMapper.findCreatorProjects(creatorId, sort, pager);
        int total = projectMapper.getProjectCnt(creatorId);
        PageResult<CreatorProjectDto> result = PageResult.of(dtos, pager, total);
        return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 프로젝트 조회 성공", result));
    }

    /**
     * 크리에이터 리뷰 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CursorPage<ReviewListDto>>> getCreatorReviews(Long creatorId, LocalDateTime lastCreatedAt, Long lastId, int size,
                                                                                    Long projectId, Boolean photoOnly) {
        loaders.creator(creatorId);

        final int pageSize = Math.max(1, size);
        final int limit = pageSize + 1;

        Long totalCount = communityMapper.countCreatorCommunity(creatorId, projectId, photoOnly);

        List<ReviewListDto.Review> communityRows = communityMapper.findCreatorCommunityWindow(creatorId, lastCreatedAt, lastId, limit, projectId, photoOnly);

        final boolean hasNext = communityRows.size() == limit;

        if (communityRows.isEmpty()) {
            return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 리뷰 조회 성공",
                    CursorPage.empty()));
        }

        final List<ReviewListDto.Review> slice = hasNext ? communityRows.subList(0, pageSize) : communityRows;

        List<Long> userIds = slice.stream().map(ReviewListDto.Review::getUserId).distinct().toList();
        List<Long> projectIds = slice.stream().map(ReviewListDto.Review::getProjectId).distinct().toList();
        List<Long> cmIds = slice.stream().map(ReviewListDto.Review::getCmId).distinct().toList();

        Map<Number, ReviewListDto.UserInfo> userMap = userMapper.selectUsersByIds(userIds).stream()
                .collect(Collectors.toMap(ReviewListDto.UserInfo::getUserId, r -> r));

        Map<Long, ReviewListDto.ProjectInfo> projectMap = projectMapper.selectProjectsByIds(projectIds).stream()
                .collect(Collectors.toMap(ReviewListDto.ProjectInfo::getProjectId, r -> r));

        Map<Long, List<String>> imagesByCmId = new HashMap<>();
        communityMapper.selectReviewImagesByCmIds(cmIds).forEach(r -> {
            Long cmId = r.getCmId();
            String url = r.getUrl();
            imagesByCmId.computeIfAbsent(cmId, k -> new ArrayList<>()).add(url);
        });

        List<ReviewListDto> reviews = communityRows.stream().map(row -> {
            Long cmId = (row.getCmId() != null) ? row.getCmId() : null;
            Long userId = (row.getUserId() != null) ? row.getUserId() : null;
            Long projectIdVal = (row.getProjectId() != null) ? row.getProjectId() : null;

            ReviewListDto.UserInfo user = userMap.get(userId);
            ReviewListDto.ProjectInfo project = projectMap.get(projectIdVal);

            ReviewListDto.UserInfo userInfo = new ReviewListDto.UserInfo();
            if (user != null) {
                userInfo.setUserId(userId);
                userInfo.setNickname(user.getNickname());
                userInfo.setProfileImg(user.getProfileImg());
            }

            ReviewListDto.ProjectInfo projectInfo = new ReviewListDto.ProjectInfo();
            if (project != null) {
                projectInfo.setProjectId(projectIdVal);
                projectInfo.setTitle(project.getTitle());
                projectInfo.setThumbnail(project.getThumbnail());
            }

            return ReviewListDto.builder()
                    .cmId(cmId)
                    .cmContent(row.getCmContent())
                    .createdAt(row.getCreatedAt())
                    .user(userInfo)
                    .project(projectInfo)
                    .images(imagesByCmId.getOrDefault(cmId, List.of()))
                    .build();
        }).toList();

        Cursor cursor = null;
        if (hasNext && !reviews.isEmpty()) {
            ReviewListDto last = reviews.getLast();
            cursor = new Cursor(last.getCreatedAt(), last.getCmId());
        }

        CursorPage<ReviewListDto> page = new CursorPage<>(reviews, cursor, hasNext, totalCount);
        return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 리뷰 조회 성공", page));
    }
}
