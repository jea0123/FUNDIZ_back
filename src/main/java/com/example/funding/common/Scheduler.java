package com.example.funding.common;

import com.example.funding.enums.NotificationType;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.SettlementMapper;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
@Transactional
public class Scheduler {

    private final ProjectMapper projectMapper;
    private final SettlementMapper settlementMapper;
    private final CreatorMapper creatorMapper;

    private final NotificationPublisher notificationPublisher;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void updateProjectStatusesDaily() {
        int opened = projectMapper.updateProjectsToOpen();
        int success = projectMapper.updateProjectsToSuccess();
        int failed = projectMapper.updateProjectsToFailed();

        List<Project> openingProjects = projectMapper.getProjectToOpen();
        for(Project project : openingProjects) {
            Creator creator = creatorMapper.findById(project.getCreatorId());
            notificationPublisher.publish(creator.getUserId(), NotificationType.FUNDING_OPEN, project.getTitle(), project.getProjectId());
        }

        List<Project> successfulProjects = projectMapper.getProjectToSuccess();
        for(Project project : successfulProjects) {
            Creator creator = creatorMapper.findById(project.getCreatorId());
            notificationPublisher.publish(creator.getUserId(), NotificationType.FUNDING_SUCCESS, project.getTitle(), project.getProjectId());
        }

        List<Project> failedProjects = projectMapper.getProjectToFailed();
        for(Project project : failedProjects) {
            Creator creator = creatorMapper.findById(project.getCreatorId());
            notificationPublisher.publish(creator.getUserId(), NotificationType.FUNDING_FAILURE, project.getTitle(), project.getProjectId());
        }

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
