package com.yeongjin.alarmserver.domain.alarm.controller;

import com.yeongjin.alarmserver.domain.alarm.dto.request.SendImmediateAlarmReq;
import com.yeongjin.alarmserver.domain.alarm.dto.request.SendScheduledAlarmReq;
import com.yeongjin.alarmserver.domain.alarm.service.SendAlarmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
public class SendAlarmController {
    private final SendAlarmService sendAlarmService;

    @PostMapping("/immediate")
    public ResponseEntity<Void> sendImmediateEmail(@RequestBody @Valid SendImmediateAlarmReq sendImmediateAlarmReq) {
        Long alarmId = sendAlarmService.sendImmediateEmail(sendImmediateAlarmReq);
        return ResponseEntity
                .created(URI.create("/emails/" + alarmId))
                .build();
    }

    @PostMapping("/scheduled")
    public ResponseEntity<Void> sendScheduledEmail(@RequestBody @Valid SendScheduledAlarmReq sendScheduledAlarmReq) {
        Long alarmId = sendAlarmService.sendScheduledEmail(sendScheduledAlarmReq);
        return ResponseEntity
                .created(URI.create("/emails/" + alarmId))
                .build();
    }
}
