package com.yeongjin.alarmserver.domain.alarm.service;

import com.yeongjin.alarmserver.domain.alarm.dto.request.SendImmediateAlarmReq;
import com.yeongjin.alarmserver.domain.alarm.dto.request.SendScheduledAlarmReq;
import com.yeongjin.alarmserver.domain.alarm.entity.Alarm;
import com.yeongjin.alarmserver.domain.alarm.entity.AlarmType;
import com.yeongjin.alarmserver.domain.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendAlarmService {
    private final AlarmRepository alarmRepository;
    private final SendAlarmPublisher sendEmailPublisher;

    public Long sendImmediateEmail(SendImmediateAlarmReq sendImmediateAlarmReq) {
        Alarm alarm = alarmRepository.save(
                Alarm.ofImmediate(
                        AlarmType.EMAIL,
                        sendImmediateAlarmReq.getRecipients(),
                        sendImmediateAlarmReq.getSubject(),
                        sendImmediateAlarmReq.getContent()
                )
        );
        sendEmailPublisher.publishToRedis(alarm);
        return alarm.getId();
    }

    @Transactional
    public Long sendScheduledEmail(SendScheduledAlarmReq sendScheduledAlarmReq) {
        Alarm alarm = alarmRepository.save(
                Alarm.ofScheduled(
                        AlarmType.EMAIL,
                        sendScheduledAlarmReq.getRecipients(),
                        sendScheduledAlarmReq.getSubject(),
                        sendScheduledAlarmReq.getContent(),
                        sendScheduledAlarmReq.getSendTime()
                )
        );
        return alarm.getId();
    }
}
