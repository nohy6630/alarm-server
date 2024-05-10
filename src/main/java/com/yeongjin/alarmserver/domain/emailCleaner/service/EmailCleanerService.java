package com.yeongjin.alarmserver.domain.emailCleaner.service;

import com.yeongjin.alarmserver.domain.repository.EmailAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailCleanerService {
    private final EmailAlarmRepository emailAlarmRepository;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void deleteSentEmails() {
        List<Long> ids = emailAlarmRepository.findIdsIsSent();
        emailAlarmRepository.deleteAllByIdInBatch(ids);
    }
}
