package com.yeongjin.alarmserver.domain.emailalarm.service;

import com.yeongjin.alarmserver.domain.emailalarm.dto.request.ImmediateEmailReq;
import com.yeongjin.alarmserver.domain.emailalarm.dto.request.ScheduledEmailReq;
import com.yeongjin.alarmserver.domain.emailalarm.entity.EmailAlarm;
import com.yeongjin.alarmserver.domain.emailalarm.repository.EmailAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailAlarmService {
    private final EmailAlarmRepository emailAlarmRepository;

    @Transactional
    public Long sendImmediateEmail(ImmediateEmailReq immediateEmailReq) {
        EmailAlarm emailAlarm = emailAlarmRepository.save(
                EmailAlarm.ofImmediate(
                        immediateEmailReq.getRecipients(),
                        immediateEmailReq.getSubject(),
                        immediateEmailReq.getContent()
                )
        );
        return emailAlarm.getId();
    }

    @Transactional
    public Long sendScheduledEmail(ScheduledEmailReq scheduledEmailReq) {
        EmailAlarm emailAlarm = emailAlarmRepository.save(
                EmailAlarm.ofScheduled(
                        scheduledEmailReq.getRecipients(),
                        scheduledEmailReq.getSubject(),
                        scheduledEmailReq.getContent(),
                        scheduledEmailReq.getSendTime()
                )
        );
        return emailAlarm.getId();
    }
}
