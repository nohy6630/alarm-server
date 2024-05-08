package com.yeongjin.alarmserver.domain.emailalarm.controller;

import com.yeongjin.alarmserver.domain.emailalarm.dto.request.ImmediateEmailReq;
import com.yeongjin.alarmserver.domain.emailalarm.dto.request.ScheduledEmailReq;
import com.yeongjin.alarmserver.domain.emailalarm.service.EmailAlarmService;
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
public class EmailAlarmController {
    private final EmailAlarmService emailAlarmService;

    @PostMapping("/immediate")
    public ResponseEntity<Void> sendImmediateEmail(@RequestBody @Valid ImmediateEmailReq immediateEmailReq) {
        Long alarmId = emailAlarmService.sendImmediateEmail(immediateEmailReq);
        return ResponseEntity
                .created(URI.create("/emails/" + alarmId))
                .build();
    }

    @PostMapping("/scheduled")
    public ResponseEntity<Void> sendScheduledEmail(@RequestBody @Valid ScheduledEmailReq scheduledEmailReq) {
        Long alarmId = emailAlarmService.sendScheduledEmail(scheduledEmailReq);
        return ResponseEntity
                .created(URI.create("/emails/" + alarmId))
                .build();
    }
}
