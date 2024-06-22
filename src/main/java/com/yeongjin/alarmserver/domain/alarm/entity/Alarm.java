package com.yeongjin.alarmserver.domain.alarm.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Comment("메일")
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> recipients;

    @Comment("제목")
    private String subject;

    @Comment("내용")
    private String content;

    @Comment("메일 발송 시간")
    private LocalDateTime sendTime;

    @Comment("알림 상태")
    @Enumerated(EnumType.STRING)
    private AlarmStatus status;

    public static Alarm ofImmediate(List<String> recipients, String subject, String content) {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        return Alarm.builder()
                .recipients(recipients)
                .subject(subject)
                .content(content)
                .sendTime(now.withSecond(0).withNano(0))
                .status(AlarmStatus.SENT)
                .build();
    }

    public static Alarm ofScheduled(List<String> recipients, String subject, String content, LocalDateTime sendTime) {
        return Alarm.builder()
                .recipients(recipients)
                .subject(subject)
                .content(content)
                .sendTime(sendTime.withSecond(0).withNano(0))
                .status(AlarmStatus.RESERVED)
                .build();
    }

    public void setStatus(AlarmStatus status) {
        this.status = status;
    }
}
