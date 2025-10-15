package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.exception.forbidden.AccessDeniedException;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.exception.notfound.ProjectNotFoundException;
import com.example.funding.exception.notfound.RewardNotFoundException;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.model.Project;
import com.example.funding.model.Reward;
import com.example.funding.service.RewardService;
import com.example.funding.service.validator.ProjectInputValidator;
import com.example.funding.service.validator.ProjectTransitionGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RewardServiceImpl implements RewardService {

    private final RewardMapper rewardMapper;
    private final ProjectMapper projectMapper;
    private final CreatorMapper creatorMapper;
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
    public void createReward(Long projectId, List<RewardCreateRequestDto> rewardList, LocalDate endDate, boolean validated) {
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
    public void replaceRewards(Long projectId, List<RewardCreateRequestDto> rewardList, LocalDate endDate) {
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
        if (projectMapper.findById(projectId) == null) throw new ProjectNotFoundException();
        if (rewardMapper.findById(rewardId) == null) throw new RewardNotFoundException();
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
    public ResponseEntity<ResponseDto<List<Reward>>> getCreatorRewardList(Long projectId, Long creatorId) {
        if (creatorMapper.findById(creatorId) == null) throw new CreatorNotFoundException();
        Project existingProject = projectMapper.findById(projectId);
        if (existingProject == null) throw new ProjectNotFoundException();
        if (!existingProject.getCreatorId().equals(creatorId)) throw new AccessDeniedException();
        // Guard
        transitionGuard.ensureProjectOwner(projectId, creatorId);

        List<Reward> rewardList = rewardMapper.getCreatorRewardList(projectId, creatorId);

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
        if (creatorMapper.findById(creatorId) == null) throw new CreatorNotFoundException();
        Project existingProject = projectMapper.findById(projectId);
        if (existingProject == null) throw new ProjectNotFoundException();
        if (!existingProject.getCreatorId().equals(creatorId)) throw new AccessDeniedException();
        // Guard
        transitionGuard.ensureProjectOwner(projectId, creatorId);
        transitionGuard.requireStatusIn(projectId, "UPCOMING", "OPEN");

        // Validator
        LocalDate endDate = projectMapper.getProjectEndDate(projectId);
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
