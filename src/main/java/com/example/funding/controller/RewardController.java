package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project/{projectId}/reward")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    /**
     * <p>리워드 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @param rewardId 리워드 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-11
     */
    @DeleteMapping("/{rewardId}")
    public ResponseEntity<ResponseDto<String>> deleteReward(@PathVariable Long projectId,
                                                            @PathVariable Long rewardId) {
        return rewardService.deleteReward(projectId, rewardId);
    }

}
