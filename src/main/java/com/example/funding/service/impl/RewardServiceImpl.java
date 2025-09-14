package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.dto.request.reward.RewardUpdateRequestDto;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.model.Reward;
import com.example.funding.service.RewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RewardServiceImpl implements RewardService {

    private final RewardMapper rewardMapper;
    private final ProjectMapper projectMapper;

    /**
     * <p>리워드 생성</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardList List<RewardCreateRequestDto>
     * @author by: 조은애
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
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> addReward(Long projectId, RewardCreateRequestDto dto) {
        Reward reward = Reward.builder()
                    .projectId(projectId)
                .rewardName(dto.getRewardName())
                .price(dto.getPrice())
                .rewardContent(dto.getRewardContent())
                .deliveryDate(dto.getDeliveryDate())
                .rewardCnt((dto.getRewardCnt() == null) ? Integer.MAX_VALUE : dto.getRewardCnt()) // 무제한 처리
                .isPosting(dto.getIsPosting())
                .remain((dto.getRewardCnt() == null) ? Integer.MAX_VALUE : dto.getRewardCnt()) // 초기 remain = rewardCnt
                .build();

        int result = rewardMapper.saveReward(reward);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "리워드 추가 실패"));
        }

        return ResponseEntity.ok(ResponseDto.success(200, "리워드 추가 성공", null));
    }

    /**
     * <p>리워드 수정</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardId 리워드 ID
     * @param dto RewardUpdateRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateReward(Long projectId, Long rewardId, RewardUpdateRequestDto dto) {
        dto.setProjectId(projectId);
        dto.setRewardId(rewardId);
        //프로젝트 상태 조회
        String status = projectMapper.getStatus(projectId);
        //심사 요청 이전에만 리워드 수정 가능
        if ("DRAFT".equals(status)) {
            //무제한 처리
            Integer reqCnt = dto.getRewardCnt();
            int newCnt = (reqCnt == null) ? Integer.MAX_VALUE : reqCnt;
            dto.setRewardCnt(newCnt);
            dto.setRemain(newCnt);

            //리워드 수정 처리
            int result = rewardMapper.updateReward(dto);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "리워드 수정 실패"));
            }
            return ResponseEntity.ok(ResponseDto.success(200, "리워드 수정 성공", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(400, "심사 요청 이후에는 리워드를 수정할 수 없습니다."));
    }

    /**
     * <p>리워드 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardId 리워드 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<String>> deleteReward(Long projectId, Long rewardId) {
        //프로젝트 상태 조회
        String status = projectMapper.getStatus(projectId);
        //심사 요청 이전에만 리워드 삭제 가능
        if ("DRAFT".equals(status)) {
            int result = rewardMapper.deleteReward(projectId, rewardId);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "리워드 삭제 실패"));
            }
        }
        return ResponseEntity.ok(ResponseDto.success(200, "리워드 삭제 성공", null));
    }
}
