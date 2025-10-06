package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.ProjectUpdateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.response.creator.CreatorPDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectListDto;
import com.example.funding.mapper.*;
import com.example.funding.model.Project;
import com.example.funding.model.Subcategory;
import com.example.funding.model.Tag;
import com.example.funding.service.CreatorService;
import com.example.funding.service.RewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreatorServiceImpl implements CreatorService {
    private final CreatorMapper creatorMapper;
    private final ProjectMapper projectMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final RewardService rewardService;
    private final RewardMapper rewardMapper;

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
     * @param dto SearchCreatorProjectDto
     * @param pager pager
     * @return 성공 시 200 OK
     * @since 2025-10-05
     * @author 조은애
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
     * @since 2025-10-05
     * @author 조은애
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CreatorProjectDetailDto>> getProjectDetail(Long projectId, Long creatorId) {
        CreatorProjectDetailDto dto = creatorMapper.getProjectDetail(projectId, creatorId);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "프로젝트를 찾을 수 없습니다."));
        }

        dto.setTagList(tagMapper.getTagList(projectId));
        dto.setRewardList(rewardMapper.getRewardList(projectId));

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 상세 조회 성공", dto));
    }

    /**
     * <p>프로젝트 생성</p>
     *
     * @param dto ProjectCreateRequestDto
     * @param creatorId 사용자 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-09
     */
    @Override
    public ResponseEntity<ResponseDto<String>> createProject(ProjectCreateRequestDto dto, Long creatorId) {
        if (dto.getSubctgrId() == null) {
            throw new IllegalArgumentException("유효하지 않은 서브카테고리 ID 입니다.");
        }

        Subcategory subcategory = categoryMapper.findSubcategoryById(dto.getSubctgrId());
        if (subcategory == null) {
            throw new IllegalArgumentException("존재하지 않는 서브카테고리 입니다.");
        }

        //TODO: 소유자 체크
        dto.setCreatorId(creatorId);

        //프로젝트 생성
        int result = creatorMapper.saveProject(dto);
        if (result == 0) {
            throw new IllegalStateException("프로젝트 생성 실패");
        }

        Long projectId = dto.getProjectId();

        //태그 저장
        List<String> tagList = dto.getTagList();
        if (tagList != null && !tagList.isEmpty()) {
            //정규화, 중복 제거, 최대 10개 제한
            List<String> normalized = tagList.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s-> s.length() > 20 ? s.substring(0, 20) : s)
                .distinct()
                .limit(10)
                .toList();

            for (String tagName : normalized) {
                int saved = tagMapper.saveTag(projectId, tagName);
                if (saved == 0) {
                    throw new IllegalStateException("태그 저장 실패");
                }
            }
        }

        //리워드 생성
        if (dto.getRewardList() != null && !dto.getRewardList().isEmpty()) {
            rewardService.createReward(projectId, dto.getRewardList());
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 생성 성공", null));
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param dto ProjectUpdateRequestDto
     * @param creatorId 사용자 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-16
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateProject(ProjectUpdateRequestDto dto, Long creatorId) {
        if (dto.getProjectId() == null) {
            throw new IllegalArgumentException("프로젝트 ID가 필요합니다.");
        }

        Project existing = projectMapper.findById(dto.getProjectId());
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "프로젝트를 찾을 수 없습니다."));
        }


        //TODO: 소유자 체크

        //프로젝트 상태 조회
        String status = projectMapper.getStatus(dto.getProjectId());
        if (!"DRAFT".equalsIgnoreCase(status)) {
            throw new IllegalStateException("DRAFT 상태에서만 프로젝트를 수정할 수 있습니다.");
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
            throw new IllegalStateException("프로젝트 수정 실패");
        }

        //태그 전체 삭제 후 저장
        tagMapper.deleteTags(dto.getProjectId());
        if (dto.getTagList() != null) {
            for (Tag tag : dto.getTagList()) {
                int saved = tagMapper.saveTag(dto.getProjectId(), tag.getTagName());
                if (saved == 0) {
                    throw new IllegalStateException("태그 저장 실패");
                }
            }
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 수정 성공", null));
    }

    /**
     * <p>프로젝트 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-16
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteProject(Long projectId, Long creatorId) {

        //소유자 체크

        //프로젝트 상태 조회
        String status = projectMapper.getStatus(projectId);
        if (!"DRAFT".equalsIgnoreCase(status)) {
            throw new IllegalStateException("DRAFT 상태에서만 프로젝트를 삭제할 수 있습니다.");
        }

        rewardMapper.deleteRewards(projectId);
        tagMapper.deleteTags(projectId);

        int result = creatorMapper.deleteProject(projectId);
        if (result == 0) {
            throw new IllegalStateException("프로젝트 삭제 실패");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 삭제 성공", null));
    }

    /**
     * <p>프로젝트 심사 요청</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-18
     */
    @Override
    public ResponseEntity<ResponseDto<String>> verifyProject(Long projectId, Long creatorId) {

        //소유자 체크

        Project project = projectMapper.findById(projectId);
        if (project == null) {
            throw new IllegalStateException("존재하지 않는 프로젝트입니다.");
        }
//        if (!project.getCreatorId().equals(creatorId)) {
//            throw new IllegalStateException("심사 요청 권한이 없습니다.");
//        }
        if (!"DRAFT".equalsIgnoreCase(project.getProjectStatus())) {
            throw new IllegalStateException("현재 상태에서는 심사 요청을 할 수 없습니다.");
        }
        if ("Y".equalsIgnoreCase(String.valueOf(project.getIsVerified()))) {
            throw new IllegalStateException("이미 심사 요청된 프로젝트입니다.");
        }

        //검증
//        List<String> errors = validator.validateAll(projectId, project);
//        if (!errors.isEmpty()) {
//            throw new IllegalArgumentException("심사 요청 검증 실패 : " + String.join(", ", errors));
//        }

        //상태 업데이트
        int result = creatorMapper.markVerifyProject(projectId);
        if (result == 0) {
            throw new IllegalStateException("심사 요청 실패");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 심사 요청 성공", null));
    }

}
