package com.yeongjin.alarmserver.domain.api.service;

import com.yeongjin.alarmserver.domain.api.dto.request.ImmediateEmailReq;
import com.yeongjin.alarmserver.domain.api.dto.request.ScheduledEmailReq;
import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import com.yeongjin.alarmserver.domain.repository.EmailAlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EmailAlarmService {
    private final EmailAlarmRepository emailAlarmRepository;
    private final JavaMailSender javaMailSender;

    private void saveEmailAlarmToStream(EmailAlarm emailAlarm) {

//        List<ObjectRecord<String, EmailAlarm>> records = redisTemplate.opsForStream()
//                .read(EmailAlarm.class, StreamOffset.fromStart("emailAlarmStream"));
//        records.stream()
//                .map(ObjectRecord::getValue)
//                .forEach(System.out::println);

    }

    @Transactional
    public Long sendImmediateEmail(ImmediateEmailReq immediateEmailReq) {
        EmailAlarm emailAlarm = emailAlarmRepository.save(
                EmailAlarm.ofImmediate(
                        immediateEmailReq.getRecipients(),
                        immediateEmailReq.getSubject(),
                        immediateEmailReq.getContent()
                )
        );

        saveEmailAlarmToStream(emailAlarm);

//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        try {
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//            mimeMessageHelper.setTo(immediateEmailReq.getRecipients().toArray(new String[0]));
//            mimeMessageHelper.setSubject(immediateEmailReq.getSubject());
//            mimeMessageHelper.setText(immediateEmailReq.getContent(), false);
//            javaMailSender.send(mimeMessage);
//            log.info("Success");
//        } catch (MessagingException e) {
//            log.info("Fail");
//            throw new RuntimeException(e);
//        }

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
