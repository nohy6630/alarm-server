package com.yeongjin.alarmserver.domain.api.service;

import com.yeongjin.alarmserver.domain.api.dto.request.ImmediateEmailReq;
import com.yeongjin.alarmserver.domain.api.dto.request.ScheduledEmailReq;
import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import com.yeongjin.alarmserver.domain.emailScheduler.service.SendEmailPublisher;
import com.yeongjin.alarmserver.domain.emailSender.service.SendEmailSubscriber;
import com.yeongjin.alarmserver.domain.repository.EmailAlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EmailAlarmService {
    private final EmailAlarmRepository emailAlarmRepository;
    private final SendEmailPublisher sendEmailPublisher;
    private final SendEmailSubscriber sendEmailSubscriber;
    private final JavaMailSender javaMailSender;

    @Transactional
    public Long sendImmediateEmail(ImmediateEmailReq immediateEmailReq) {
        EmailAlarm emailAlarm = emailAlarmRepository.save(
                EmailAlarm.ofImmediate(
                        immediateEmailReq.getRecipients(),
                        immediateEmailReq.getSubject(),
                        immediateEmailReq.getContent()
                )
        );
        emailAlarm.setSent();
        sendEmailPublisher.publishToRedis(emailAlarm);
        return emailAlarm.getId();
    }

    @Transactional
    public Long sendScheduledEmail(ScheduledEmailReq scheduledEmailReq) {
        EmailAlarm emailAlarm = emailAlarmRepository.save(
                EmailAlarm.ofScheduled(
                        scheduledEmailReq.getRecipients(),
                        scheduledEmailReq.getSubject(),
                        scheduledEmailReq.getContent(),
                        scheduledEmailReq.getSendTime().withSecond(0)
                )
        );
        return emailAlarm.getId();
    }
}
