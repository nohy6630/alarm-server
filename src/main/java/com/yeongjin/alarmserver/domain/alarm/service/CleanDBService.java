package com.yeongjin.alarmserver.domain.alarm.service;

import com.yeongjin.alarmserver.domain.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CleanDBService {
    private final AlarmRepository alarmRepository;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    @Transactional
    public void deleteSentEmails() {
        List<Long> ids = alarmRepository.findIdsIsSent();
        alarmRepository.deleteAllByIdInBatch(ids);
    }
}
