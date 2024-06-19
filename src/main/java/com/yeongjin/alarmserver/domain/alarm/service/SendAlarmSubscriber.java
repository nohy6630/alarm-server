package com.yeongjin.alarmserver.domain.alarm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeongjin.alarmserver.domain.alarm.entity.Alarm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendAlarmSubscriber {
    private final JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper;

    public void sendEmail(String message, String channel) throws JsonProcessingException {
        log.info("Received message: " + message + " from channel: " + channel);
        Alarm alarm = objectMapper.readValue(message, Alarm.class);
        try {
            javaMailSender.send(createMimeMessage(alarm));
        } catch (MessagingException e) {
            log.warn("Create Mime Message Failed");
        }
    }

    private MimeMessage createMimeMessage(Alarm alarm) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(alarm.getRecipients().toArray(new String[0]));
        mimeMessageHelper.setSubject(alarm.getSubject());
        mimeMessageHelper.setText(alarm.getContent(), false);
        return mimeMessage;
    }
}
