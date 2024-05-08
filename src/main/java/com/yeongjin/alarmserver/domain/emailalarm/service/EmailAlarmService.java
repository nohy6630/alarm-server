package com.yeongjin.alarmserver.domain.emailalarm.service;

import com.yeongjin.alarmserver.domain.emailalarm.dto.request.ImmediateEmailReq;
import com.yeongjin.alarmserver.domain.emailalarm.dto.request.ScheduledEmailReq;
import com.yeongjin.alarmserver.domain.emailalarm.entity.EmailAlarm;
import com.yeongjin.alarmserver.domain.emailalarm.repository.EmailAlarmRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EmailAlarmService {
    private final EmailAlarmRepository emailAlarmRepository;
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

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(immediateEmailReq.getRecipients().toArray(new String[0]));
            mimeMessageHelper.setSubject(immediateEmailReq.getSubject());
            mimeMessageHelper.setText(immediateEmailReq.getContent(), false);
            javaMailSender.send(mimeMessage);
            log.info("Success");
        } catch (MessagingException e) {
            log.info("Fail");
            throw new RuntimeException(e);
        }

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
