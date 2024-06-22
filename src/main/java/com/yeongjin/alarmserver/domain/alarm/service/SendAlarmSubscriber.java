package com.yeongjin.alarmserver.domain.alarm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeongjin.alarmserver.domain.alarm.entity.Alarm;
import com.yeongjin.alarmserver.domain.alarm.entity.AlarmStatus;
import com.yeongjin.alarmserver.domain.alarm.repository.AlarmRepository;
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
@Slf4j
@Transactional(readOnly = true)
public class SendAlarmSubscriber {
    private final JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper;
    private final AlarmRepository alarmRepository;

    @Transactional
    public void sendEmail(String message, String channel) throws JsonProcessingException {
        log.info("Receive Message from " + channel);
        Long alarmId = objectMapper.readValue(message, Long.class);
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow();
        try {
            javaMailSender.send(createMimeMessage(alarm));
            alarm.setStatus(AlarmStatus.SENT);
        } catch (Exception e) {
            log.warn("Send Email Failed");
            alarm.setStatus(AlarmStatus.RESERVED);
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
