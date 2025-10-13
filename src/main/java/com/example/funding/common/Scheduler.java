package com.example.funding.common;

import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.SettlementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
@Transactional
public class Scheduler {

    private final ProjectMapper projectMapper;
    private final SettlementMapper settlementMapper;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void updateProjectStatusesDaily() {
        int opened = projectMapper.updateProjectsToOpen();
        int success = projectMapper.updateProjectsToSuccess();
        int failed = projectMapper.updateProjectsToFailed();

        log.info("[ProjectStatusScheduler] OPEN={}, SUCCESS={}, FAILED={}", opened, success, failed);
    }

    /**
     * 매일 자정 30분에 정산 대기 내역을 생성.
     * 수수료율은 10%로 설정.
     */
    @Scheduled(cron = "0 30 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateProjectSettlementWaitingDaily() {
        double feeRate = 0.10;
        String statusColumn = "settlementStatus";

        int inserted = settlementMapper.bulkInsertSettlementWaiting(feeRate, statusColumn);

        log.info("[SettlementScheduler] waiting inserted: {}", inserted);
    }
}
