package com.schedlock.service;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class SchedulerService {

    @Scheduled(fixedDelay = 2000)
    @SchedulerLock(name = "scheduledUniqueNameTask",lockAtMostFor = "5m",lockAtLeastFor = "2s")
    public void executeTask() throws InterruptedException {
        log.info("Executing task  at {}  ", new Date());
        Thread.sleep(3000);
    }

}
