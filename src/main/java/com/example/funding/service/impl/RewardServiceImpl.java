package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.dto.request.reward.RewardUpdateRequestDto;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.model.Reward;
import com.example.funding.service.RewardService;
import com.example.funding.service.validator.ProjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RewardServiceImpl implements RewardService {

    private final RewardMapper rewardMapper;
    private final ProjectMapper projectMapper;
    private final ProjectValidator projectValidator;

    /**
     * <p>리워드 생성</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardList List<RewardCreateRequestDto>
     * @author 조은애
     * @since 2025-09-09
     */
    @Override
    public void createReward(Long projectId, List<RewardCreateRequestDto> rewardList) {
        for (RewardCreateRequestDto dto : rewardList) {
            Reward reward = Reward.builder()
                    .projectId(projectId)
                    .rewardName(dto.getRewardName())
                    .price(dto.getPrice())
                    .rewardContent(dto.getRewardContent())
                    .deliveryDate(dto.getDeliveryDate())
                    .rewardCnt(dto.getRewardCnt())
                    .isPosting(dto.getIsPosting())
                    .remain(dto.getRewardCnt())
                    .build();

            rewardMapper.saveReward(reward);
        }
    }

    /**
     * <p>리워드 추가</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto RewardCreateRequestDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> addReward(Long projectId, RewardCreateRequestDto dto) {
        Reward reward = Reward.builder()
                .projectId(projectId)
                .rewardName(dto.getRewardName().trim())
                .price(dto.getPrice())
                .rewardContent(dto.getRewardContent().trim())
                .deliveryDate(dto.getDeliveryDate())
                .rewardCnt((dto.getRewardCnt() == null) ? Integer.MAX_VALUE : dto.getRewardCnt()) // 무제한 처리
                .isPosting(dto.getIsPosting())
                .remain((dto.getRewardCnt() == null) ? Integer.MAX_VALUE : dto.getRewardCnt()) // 초기 remain = rewardCnt
                .build();

        int result = rewardMapper.saveReward(reward);
        if (result == 0) {
            throw new IllegalStateException("리워드 추가 실패");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "리워드 추가 성공", null));
    }

    /**
     * <p>리워드 수정</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardId 리워드 ID
     * @param dto RewardUpdateRequestDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateReward(Long projectId, Long rewardId, RewardUpdateRequestDto dto) {
        dto.setProjectId(projectId);
        dto.setRewardId(rewardId);

        //프로젝트 상태 조회
        String status = projectMapper.getStatus(projectId);
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException("현재 상태에서는 리워드를 수정할 수 없습니다.");
        }

        //무제한 처리
        Integer reqCnt = dto.getRewardCnt();
        int newCnt = (reqCnt == null) ? Integer.MAX_VALUE : reqCnt;
        dto.setRewardCnt(newCnt);
        dto.setRemain(newCnt);

        //리워드 수정 처리
        int result = rewardMapper.updateReward(dto);
        if (result == 0) {
            throw new IllegalStateException("리워드 수정 실패");
        }
        return ResponseEntity.ok(ResponseDto.success(200, "리워드 수정 성공", null));
    }

    /**
     * <p>리워드 전체 삭제 후 저장</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardList 리워드 리스트
     * @author 조은애
     * @since 2025-10-07
     */
    @Override
    public void replaceRewards(Long projectId, List<RewardCreateRequestDto> rewardList) {
        //프로젝트 상태 조회
        String status = projectMapper.getStatus(projectId);
        if (!"DRAFT".equalsIgnoreCase(status)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "DRAFT 상태에서만 리워드를 교체할 수 있습니다.");
        }

        List<String> errors = projectValidator.validateRewardsDtos(rewardList);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }

        rewardMapper.deleteRewards(projectId);

        if (rewardList == null || rewardList.isEmpty()) return;

        for (RewardCreateRequestDto dto : rewardList) {
            Reward reward = Reward.builder()
                .projectId(projectId)
                .rewardName(dto.getRewardName().trim())
                .price(dto.getPrice())
                .rewardContent(dto.getRewardContent().trim())
                .deliveryDate(dto.getDeliveryDate())
                .rewardCnt((dto.getRewardCnt() == null) ? Integer.MAX_VALUE : dto.getRewardCnt()) // 무제한 처리
                .isPosting(dto.getIsPosting())
                .remain((dto.getRewardCnt() == null) ? Integer.MAX_VALUE : dto.getRewardCnt()) // 초기 remain = rewardCnt
                .build();

            int saved = rewardMapper.saveReward(reward);
            if (saved == 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "리워드 저장 실패");
            }
        }
    }

    /**
     * <p>리워드 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardId 리워드 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteReward(Long projectId, Long rewardId) {
        //프로젝트 상태 조회
        String status = projectMapper.getStatus(projectId);
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException("현재 상태에서는 리워드를 삭제할 수 없습니다.");
        }

        int result = rewardMapper.deleteReward(projectId, rewardId);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "존재하지 않는 리워드입니다."));
        }

        return ResponseEntity.ok(ResponseDto.success(200, "리워드 삭제 성공", null));
    }
}
