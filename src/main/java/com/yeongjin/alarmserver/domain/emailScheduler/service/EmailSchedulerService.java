package com.yeongjin.alarmserver.domain.emailScheduler.service;

import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import com.yeongjin.alarmserver.domain.emailSender.service.EmailSenderService;
import com.yeongjin.alarmserver.domain.repository.EmailAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSchedulerService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailAlarmRepository emailAlarmRepository;
    private final EmailSenderService emailSenderService;

    //todo 동시성 처리
    public void registerEmailAlarmToStream(EmailAlarm emailAlarm) {
        redisTemplate.opsForStream()
                .add(StreamRecords.newRecord()
                        .in("emailAlarmStream")
                        .ofObject(emailAlarm));
        emailAlarm.setSent();
    }

    @Scheduled(cron = "30 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void scanEmailAlarmInDatabase() {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        List<EmailAlarm> emailAlarms = emailAlarmRepository.findEmailsToSend(now.withSecond(0));
        for (EmailAlarm emailAlarm : emailAlarms) {
            //registerEmailAlarmToStream(emailAlarm);
            emailAlarm.setSent();
            emailSenderService.sendEmail(emailAlarm);
        }
    }
}
