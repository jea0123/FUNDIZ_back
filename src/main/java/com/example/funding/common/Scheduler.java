package com.example.funding.common;

import com.example.funding.mapper.ProjectMapper;
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

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void updateProjectStatusesDaily() {
        int opened = projectMapper.updateProjectsToOpen();
        int success = projectMapper.updateProjectsToSuccess();
        int failed = projectMapper.updateProjectsToFailed();

        log.info("[ProjectStatusScheduler] OPEN={}, SUCCESS={}, FAILED={}", opened, success, failed);
    }
}
