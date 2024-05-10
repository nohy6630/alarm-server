package com.yeongjin.alarmserver.domain.emailSender.service;

import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void createStreamGroup() {
        try {
            redisTemplate.opsForStream().createGroup("emailAlarmStream", "myGroup");
        } catch (Exception e) {
            log.info("Group already exists");
        }
    }

    private void sendEmail(EmailAlarm emailAlarm) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailAlarm.getRecipients().toArray(new String[0]));
            mimeMessageHelper.setSubject(emailAlarm.getSubject());
            mimeMessageHelper.setText(emailAlarm.getContent(), false);
            javaMailSender.send(mimeMessage);
            log.info("Send Success");
        } catch (MessagingException e) {
            log.info("Send Fail");
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void fetchDataFromStream() {
        List<ObjectRecord<String, EmailAlarm>> records = redisTemplate.opsForStream()
                .read(EmailAlarm.class, StreamOffset.fromStart("emailAlarmStream"));
        records.stream()
                .map(ObjectRecord::getValue)
                .forEach(this::sendEmail);
        records.stream()
                .map(ObjectRecord::getId)
                .forEach(recordId -> redisTemplate.opsForStream()
                        .acknowledge("emailAlarmStream", "myGroup", recordId));
    }
}
