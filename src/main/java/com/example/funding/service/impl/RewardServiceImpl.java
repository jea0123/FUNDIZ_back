package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.model.Project;
import com.example.funding.model.Reward;
import com.example.funding.service.RewardService;
import com.example.funding.service.validator.ProjectInputValidator;
import com.example.funding.service.validator.ProjectTransitionGuard;
import com.example.funding.validator.Loaders;
import com.example.funding.validator.PermissionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class RewardServiceImpl implements RewardService {
    private final Loaders loaders;
    private final PermissionChecker auth;
    private final RewardMapper rewardMapper;
    private final ProjectMapper projectMapper;
    private final ProjectInputValidator inputValidator;
    private final ProjectTransitionGuard transitionGuard;

    /**
     * <p>리워드 생성</p>
     *
     * @param projectId  프로젝트 ID
     * @param rewardList List<RewardCreateRequestDto>
     * @author 조은애
     * @since 2025-09-09
     */
    @Override
    public void createReward(Long projectId, List<RewardCreateRequestDto> rewardList, LocalDateTime endDate, boolean validated) {
        loaders.project(projectId);
        // Guard
        transitionGuard.requireDraft(projectId);

        // Validator
        if (!validated) {
            List<String> errors = inputValidator.validateRewards(rewardList, endDate);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
            }
        }

        for (RewardCreateRequestDto dto : rewardList) {
            Reward reward = Reward.builder()
                    .projectId(projectId)
                    .rewardName(dto.getRewardName().trim())
                    .price(dto.getPrice())
                    .rewardContent(dto.getRewardContent().trim())
                    .deliveryDate(dto.getDeliveryDate())
                    .rewardCnt(dto.getRewardCnt())
                    .isPosting(dto.getIsPosting())
                    .build();

            rewardMapper.saveReward(reward);
        }
    }

    /**
     * <p>프로젝트 리워드 교체</p>
     * <p>기존 리워드 모두 삭제한 뒤, 전달받은 리스트로 새로 저장</p>
     *
     * @param projectId  프로젝트 ID
     * @param rewardList 새로 등록할 리워드 리스트
     * @author 조은애
     * @since 2025-10-07
     */
    @Override
    public void replaceRewards(Long projectId, List<RewardCreateRequestDto> rewardList, LocalDateTime endDate) {
        loaders.project(projectId);
        // Guard
        transitionGuard.requireDraft(projectId);

        // Validator
        List<String> errors = inputValidator.validateRewards(rewardList, endDate);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }

        rewardMapper.deleteRewards(projectId);

        for (RewardCreateRequestDto dto : rewardList) {
            Reward reward = Reward.builder()
                    .projectId(projectId)
                    .rewardName(dto.getRewardName().trim())
                    .price(dto.getPrice())
                    .rewardContent(dto.getRewardContent().trim())
                    .deliveryDate(dto.getDeliveryDate())
                    .rewardCnt(dto.getRewardCnt())
                    .isPosting(dto.getIsPosting())
                    .build();

            rewardMapper.saveReward(reward);
        }
    }

    /**
     * <p>리워드 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardId  리워드 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteReward(Long projectId, Long rewardId) {
        // TODO: 리워드 삭제 시, 해당 리워드를 선택한 서포터가 있을 경우 예외 처리 필요
        // TODO: 크리에이터 권한 검사 추가 필요
        loaders.project(projectId);
        loaders.reward(rewardId);
        // Guard
        transitionGuard.requireDraft(projectId);

        rewardMapper.deleteReward(projectId, rewardId);
        return ResponseEntity.ok(ResponseDto.success(200, "리워드 삭제 성공", null));
    }

    /**
     * <p>리워드 목록 조회</p>
     *
     * @param projectId 프로젝트 Id
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-08
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<Reward>>> getRewardListManage(Long projectId, Long creatorId) {
        loaders.creator(creatorId);
        Project existingProject = loaders.project(projectId);
        auth.mustBeOwner(creatorId, existingProject.getCreatorId());
        // Guard
        transitionGuard.ensureProjectOwner(projectId, creatorId);

        List<Reward> rewardList = rewardMapper.getRewardListManage(projectId, creatorId);

        return ResponseEntity.ok(ResponseDto.success(200, "창작자 리워드 목록 조회 성공", rewardList));
    }

    /**
     * <p>리워드 단건 추가</p>
     *
     * @param projectId 프로젝트 Id
     * @param creatorId 창작자 ID
     * @param dto       RewardCreateRequestDto
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-08
     */
    @Override
    public ResponseEntity<ResponseDto<String>> addReward(Long projectId, Long creatorId, RewardCreateRequestDto dto) {
        loaders.creator(creatorId);
        Project existingProject = loaders.project(projectId);
        auth.mustBeOwner(creatorId, existingProject.getCreatorId());
        // Guard
        transitionGuard.ensureProjectOwner(projectId, creatorId);
        transitionGuard.requireStatusIn(projectId, "UPCOMING", "OPEN");

        // Validator
        LocalDateTime endDate = projectMapper.getProjectEndDate(projectId);
        List<String> errors = inputValidator.validateRewardFields(dto, endDate);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }

        Reward reward = Reward.builder()
                .projectId(projectId)
                .rewardName(dto.getRewardName().trim())
                .price(dto.getPrice())
                .rewardContent(dto.getRewardContent().trim())
                .deliveryDate(dto.getDeliveryDate())
                .rewardCnt(dto.getRewardCnt())
                .isPosting(dto.getIsPosting())
                .build();

        rewardMapper.saveReward(reward);
        return ResponseEntity.ok(ResponseDto.success(200, "리워드 추가 성공", null));
    }
}
