package com.yeongjin.alarmserver.domain.alarm.service;

import com.yeongjin.alarmserver.domain.alarm.dto.request.SendImmediateAlarmReq;
import com.yeongjin.alarmserver.domain.alarm.dto.request.SendScheduledAlarmReq;
import com.yeongjin.alarmserver.domain.alarm.entity.Alarm;
import com.yeongjin.alarmserver.domain.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SendAlarmService {
    private final AlarmRepository alarmRepository;
    private final SendAlarmPublisher sendEmailPublisher;
    private final SendAlarmSubscriber sendAlarmSubscriber;
    private final JavaMailSender javaMailSender;

    @Transactional
    public Long sendImmediateEmail(SendImmediateAlarmReq sendImmediateAlarmReq) {
        Alarm alarm = alarmRepository.save(
                Alarm.ofImmediate(
                        sendImmediateAlarmReq.getRecipients(),
                        sendImmediateAlarmReq.getSubject(),
                        sendImmediateAlarmReq.getContent()
                )
        );
        alarm.setSent();
        sendEmailPublisher.publishToRedis(alarm);
        return alarm.getId();
    }

    @Transactional
    public Long sendScheduledEmail(SendScheduledAlarmReq sendScheduledAlarmReq) {
        Alarm alarm = alarmRepository.save(
                Alarm.ofScheduled(
                        sendScheduledAlarmReq.getRecipients(),
                        sendScheduledAlarmReq.getSubject(),
                        sendScheduledAlarmReq.getContent(),
                        sendScheduledAlarmReq.getSendTime().withSecond(0)
                )
        );
        return alarm.getId();
    }
}
