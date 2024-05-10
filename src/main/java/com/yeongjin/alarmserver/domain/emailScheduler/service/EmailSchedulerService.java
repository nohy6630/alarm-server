package com.yeongjin.alarmserver.domain.emailScheduler.service;

import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import com.yeongjin.alarmserver.domain.repository.EmailAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSchedulerService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailAlarmRepository emailAlarmRepository;

    @Scheduled(cron = "30 * * * * *", zone = "Asia/Seoul")
    public void registerEmailAlarmToStream() {
        List<EmailAlarm> emailAlarms = emailAlarmRepository.findBySendTime(LocalDateTime.now().withSecond(0));
        for (EmailAlarm emailAlarm : emailAlarms) {
            redisTemplate.opsForStream()
                    .add(StreamRecords.newRecord()
                            .in("emailAlarmStream")
                            .ofObject(emailAlarm));
        }
    }
}
