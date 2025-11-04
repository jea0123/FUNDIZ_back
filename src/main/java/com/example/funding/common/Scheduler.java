package com.example.funding.common;

import com.example.funding.enums.NotificationType;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.BackingMapper;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.SettlementMapper;
import com.example.funding.model.Creator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
@Transactional
public class Scheduler {

    private final ProjectMapper projectMapper;
    private final SettlementMapper settlementMapper;
    private final CreatorMapper creatorMapper;
    private final BackingMapper backingMapper;

    private final NotificationPublisher notificationPublisher;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void updateProjectStatusesDaily() {
        int opened = projectMapper.updateProjectsToOpen();
        int success = projectMapper.updateProjectsToSuccess();
        int failed = projectMapper.updateProjectsToFailed();

        List<Map<String, Object>> openingProjects = projectMapper.getProjectToOpen();
        for (Map<String, Object> project : openingProjects) {
            Creator creator = creatorMapper.findById((Long) project.get("creatorId"));
            notificationPublisher.publish(creator.getUserId(), NotificationType.FUNDING_OPEN, (String) project.get("title"), (Long) project.get("projectId"));
        }

        List<Map<String, Object>> successfulProjects = projectMapper.getProjectToSuccess();
        for (Map<String, Object> project : successfulProjects) {
            Creator creator = creatorMapper.findById((Long) project.get("creatorId"));
            notificationPublisher.publish(creator.getUserId(), NotificationType.FUNDING_SUCCESS, (String) project.get("title"), (Long) project.get("projectId"));
        }

        List<Map<String, Object>> failedProjects = projectMapper.getProjectToFailed();
        for (Map<String, Object> project : failedProjects) {
            Creator creator = creatorMapper.findById((Long) project.get("creatorId"));
            notificationPublisher.publish(creator.getUserId(), NotificationType.FUNDING_FAILURE, (String) project.get("title"), (Long) project.get("projectId"));
        }

        // 프로젝트 상태에 따라 backingStatus 변경 기능 추가
        backingMapper.updateBackingStatusCompleted();
        backingMapper.updateBackingStatusCancelled();

        log.info("[ProjectStatusScheduler] OPEN={}, SUCCESS={}, FAILED={}", opened, success, failed);
    }

    /**
     * 매 시간 정산 대기 데이터를 생성.
     * 수수료율은 10%로 설정.
     */
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateProjectSettlementWaitingDaily() {
        double feeRate = 0.10;
        String statusColumn = "settlementStatus";

        int inserted = settlementMapper.bulkInsertSettlementWaiting(feeRate, statusColumn);

        log.info("[SettlementScheduler] waiting inserted: {}", inserted);
    }
}
