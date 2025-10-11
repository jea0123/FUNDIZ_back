package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.mapper.*;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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

    private final ProjectInputValidator inputValidator;
    private final ProjectTransitionGuard transitionGuard;

//    @Override
//    public ResponseEntity<ResponseDto<List<CreatorPListDto>>> getCreatorPList(Long creatorId) {
//        Creator creator =creatorMapper.findById(creatorId);
//
//        if(creator==null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "크리에이터의 프로젝트 목록을 찾을 수 없습니다."));
//        }
//        List<CreatorPListDto> creatorpList = projectMapper.getCreatorPList(creatorId);
//        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "크리에이터의 프로젝트 리스트 불러오기 성공", creatorpList));
//    }
//
//    //TODO: 해야할일
//    @Override
//    public ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDList(Long creatorId, Long projectId) {
//        CreatorPDetailDto creatorPDetail = creatorMapper.getCreatorPDetailDto(creatorId);
//
//        if (creatorPDetail==null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "창작자의 프로젝트 상세보기를 불러오지 못했습니다."));
//        }
//        return null;
//    }

    @Override
    public ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDashBoard(Long creatorId) {
        return null;
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
            .thumbnail(dto.getThumbnail())
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

        boolean isComplete = creatorMapper.hasRequiredCreatorProfile(creatorId);
        boolean isSuspended = Boolean.TRUE.equals(userMapper.suspendedCreator(creatorId));

        CreatorProfileSummaryDto summary = creatorMapper.getCreatorProfileSummary(creatorId);
        if (summary == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "창작자 프로필을 찾을 수 없습니다.");
        }
        summary.setIsComplete(isComplete);
        summary.setIsSuspended(isSuspended);

        return ResponseEntity.ok(ResponseDto.success(200, "창작자 프로필 조회 성공", summary));
    }

    private static final int MAX_TAGS = 10;
    private static final int MAX_LENGTH = 20;

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
}
