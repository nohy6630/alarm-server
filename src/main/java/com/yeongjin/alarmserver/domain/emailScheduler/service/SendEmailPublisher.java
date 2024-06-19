package com.yeongjin.alarmserver.domain.emailScheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import com.yeongjin.alarmserver.domain.emailSender.service.SendEmailSubscriber;
import com.yeongjin.alarmserver.domain.repository.EmailAlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendEmailPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailAlarmRepository emailAlarmRepository;
    private final ObjectMapper objectMapper;
    private final ChannelTopic channelTopic;

    @Scheduled(cron = "30 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void scanDatabase() {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().withSecond(0);
        log.info("Scan Database at " + now);
        List<EmailAlarm> emailAlarms = emailAlarmRepository.findEmailsToSend(now);
        for (EmailAlarm emailAlarm : emailAlarms) {
            emailAlarm.setSent();
            publishToRedis(emailAlarm);
        }
    }

    public void publishToRedis(EmailAlarm emailAlarm) {
        try {
            String message = objectMapper.writeValueAsString(emailAlarm);
            redisTemplate.convertAndSend(channelTopic.getTopic(), message);
        } catch (JsonProcessingException e) {
            log.warn("Publish Message Failed");
        }
    }
}
