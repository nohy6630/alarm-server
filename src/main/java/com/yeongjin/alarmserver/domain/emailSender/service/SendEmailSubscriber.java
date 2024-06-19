package com.yeongjin.alarmserver.domain.emailSender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
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
public class SendEmailSubscriber {
    private final JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper;

    public void sendEmail(String message, String channel) throws JsonProcessingException {
        log.info("Received message: " + message + " from channel: " + channel);
        EmailAlarm emailAlarm = objectMapper.readValue(message, EmailAlarm.class);
        try {
            javaMailSender.send(createMimeMessage(emailAlarm));
        } catch (MessagingException e) {
            log.warn("Create Mime Message Failed");
        }
    }

    private MimeMessage createMimeMessage(EmailAlarm emailAlarm) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(emailAlarm.getRecipients().toArray(new String[0]));
        mimeMessageHelper.setSubject(emailAlarm.getSubject());
        mimeMessageHelper.setText(emailAlarm.getContent(), false);
        return mimeMessage;
    }
}
