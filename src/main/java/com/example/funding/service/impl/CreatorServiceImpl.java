package com.example.funding.service.impl;

import com.example.funding.common.FileUploader;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.CreatorRegisterRequestDto;
import com.example.funding.dto.request.creator.CreatorUpdateRequestDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.request.cs.NoticeUpdateRequestDto;
import com.example.funding.dto.response.backing.BackingCreatorBackerList;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import com.example.funding.exception.AlreadyCreatorException;
import com.example.funding.exception.UserNotFoundException;
import com.example.funding.mapper.*;
import com.example.funding.model.Creator;
import com.example.funding.model.Notice;
import com.example.funding.model.Project;
import com.example.funding.service.CreatorService;
import com.example.funding.service.RewardService;
import com.example.funding.service.validator.ProjectInputValidator;
import com.example.funding.service.validator.ProjectTransitionGuard;
import com.example.funding.service.validator.ValidationRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreatorServiceImpl implements CreatorService {
    private static final int MAX_TAGS = 10;
    private static final int MAX_LENGTH = 20;
    private final CreatorMapper creatorMapper;
    private final ProjectMapper projectMapper;
    private final TagMapper tagMapper;
    private final RewardService rewardService;
    private final RewardMapper rewardMapper;
    private final UserMapper userMapper;
    private final SettlementMapper settlementMapper;
    private final BackingMapper backingMapper;
    private final ShippingMapper shippingMapper;
    private final ProjectInputValidator inputValidator;
    private final ProjectTransitionGuard transitionGuard;
    private final FileUploader fileUploader;

    private static List<String> normalizeTags(List<String> tagList) {
        //불변 빈 리스트
        if (tagList == null) return List.of();

        List<String> out = new ArrayList<>(tagList.size());
        for (String tag : tagList) {
            if (tag == null) continue;
            String display = ValidationRules.normTagDisplay(tag);
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
        // Guard: 소유자
        transitionGuard.ensureProjectOwner(projectId, creatorId);

        CreatorProjectDetailDto dto = creatorMapper.getProjectDetail(projectId, creatorId);
        dto.setTagList(tagMapper.getTagList(projectId));
        dto.setRewardList(rewardMapper.getRewardList(projectId));

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
    public ResponseEntity<ResponseDto<String>> createProject(ProjectCreateRequestDto dto, Long creatorId) {
        dto.setCreatorId(creatorId);

        // Guard
        transitionGuard.assertCanCreate(creatorId);

        // Validator
        List<String> errors = inputValidator.validateProjectCreate(dto);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }

        // 프로젝트 생성
        int result = creatorMapper.saveProject(dto);
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 생성 실패");
        }

        // 태그 저장
        List<String> normalized = normalizeTags(dto.getTagList());
        for (String tagName : normalized) {
            int saved = tagMapper.saveTag(dto.getProjectId(), tagName);
            if (saved == 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "태그 저장 실패");
            }
        }

        // 리워드 생성
        if (dto.getRewardList() != null && !dto.getRewardList().isEmpty()) {
            rewardService.createReward(dto.getProjectId(), dto.getRewardList(), dto.getEndDate(), true);
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 생성 성공", null));
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
        if (dto.getProjectId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로젝트 ID가 필요합니다.");
        }
        // Guard
        transitionGuard.assertCanUpdate(dto.getProjectId(), creatorId);
        // Validator
        List<String> errors = inputValidator.validateProjectUpdate(dto);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }

        Project project = Project.builder()
                .projectId(dto.getProjectId())
                .subctgrId(dto.getSubctgrId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .thumbnail(dto.getThumbnailUrl())
                .goalAmount(dto.getGoalAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        int result = creatorMapper.updateProject(creatorId, project);
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 수정 실패");
        }

        // 태그 전체 삭제 후 저장
        tagMapper.deleteTags(dto.getProjectId());
        List<String> normalized = normalizeTags(dto.getTagList());
        for (String tagName : normalized) {
            int saved = tagMapper.saveTag(dto.getProjectId(), tagName);
            if (saved == 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "태그 저장 실패");
            }
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
        // Guard
        transitionGuard.assertCanDelete(projectId, creatorId);

        rewardMapper.deleteRewards(projectId);
        tagMapper.deleteTags(projectId);

        int result = creatorMapper.deleteProject(projectId);
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 삭제 실패");
        }

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
        // Guard
        transitionGuard.assertCanSubmit(projectId, creatorId);

        //상태 업데이트
        int result = creatorMapper.markVerifyProject(projectId);
        if (result == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "심사 요청 실패");
        }

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
    public ResponseEntity<ResponseDto<CreatorProjectSummaryDto>> getProjectSummary(Long projectId, Long creatorId) {
        Project project = projectMapper.findById(projectId);
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다.");
        }

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
     * <li>정지 여부</li>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-11
     */
    public ResponseEntity<ResponseDto<CreatorProfileSummaryDto>> getCreatorProfileSummary(Long creatorId) {
        // Guard
        transitionGuard.ensureCreatorExistsOrThrow(creatorId);

        boolean isComplete = creatorMapper.hasRequiredCreatorProfile(creatorId) == 1;
        boolean isSuspended = userMapper.suspendedCreator(creatorId) == 1;

        CreatorProfileSummaryDto summary = creatorMapper.getCreatorProfileSummary(creatorId);
        if (summary == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "창작자 프로필을 찾을 수 없습니다.");
        }
        summary.setIsComplete(isComplete);
        summary.setIsSuspended(isSuspended);

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
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfCreator(Long creatorId, Pager pager) {
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
    public ResponseEntity<ResponseDto<CreatorDashboardDto>> getCreatorDashBoard(Long creatorId) {
        long projectTotal = projectMapper.getProjectCnt(creatorId);
        long totalAmount = settlementMapper.getTotalAmountCreatorId(creatorId);
        long totalBackingCnt = backingMapper.getBackerCnt(creatorId);
        long totalVerifyingCnt = projectMapper.getVerifyingCnt(creatorId);

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

        //TODO : 추가 대시보드(2개) 리스트 구현을위해서는 컬럼을 추가할 필요가있음 상의 후 구현

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
    public ResponseEntity<ResponseDto<List<BackingCreatorProjectListDto>>> getCreatorProjectList(Long creatorId) {
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
    public ResponseEntity<ResponseDto<List<CreatorShippingProjectList>>> getCreatorShippingList(Long creatorId) {
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
    public ResponseEntity<ResponseDto<List<CreatorShippingBackerList>>> getShippingBackerList(Long creatorId, Long projectId) {

        List<CreatorShippingBackerList> shippingBackerLists = shippingMapper.creatorShippingBackerList(creatorId, projectId);

        return ResponseEntity.ok(ResponseDto.success(200, "창작자의 배송(후원자) 리스트 조회 성공", shippingBackerLists));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> registerCreator(CreatorRegisterRequestDto dto, Long userId) throws IOException {
        if (userMapper.getUserById(userId) == null) {
            throw new UserNotFoundException();
        }
        if (userMapper.getCreatorIdByUserId(userId) != null) {
            throw new AlreadyCreatorException();
        }
        String profileImgPath = fileUploader.upload(dto.getProfileImg());
        Creator creator = Creator.builder()
                .userId(userId)
                .creatorName(dto.getCreatorName())
                .creatorType(dto.getCreatorType().name())
                .businessNum(dto.getBusinessNumber())
                .profileImg(profileImgPath)
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .account(dto.getAccount())
                .bank(dto.getBank())
                .build();
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
     * @param dto CreatorUpdateRequestDto
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-15
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateCreatorInfo(Long creatorId, CreatorUpdateRequestDto dto) {

        int result = creatorMapper.updateCreatorInfo(dto);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "창작자 정보 수정 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "창작자 정보 수정 완료", "창작자 정보 수정 "));
    }
}
