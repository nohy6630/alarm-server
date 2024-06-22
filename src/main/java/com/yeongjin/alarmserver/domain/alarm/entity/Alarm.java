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
    // 알림 유형이 EMAIL일 경우 수신자 이메일 주소
    // 알림 유형이 FCM일 경우 수신자 FCM 토큰

    @Comment("제목")
    private String subject;

    @Comment("내용")
    private String content;

    @Comment("메일 발송 시간")
    private LocalDateTime sendTime;

    @Comment("알림 상태")
    @Enumerated(EnumType.STRING)
    private AlarmStatus status;

    @Comment("알림 유형")
    @Enumerated(EnumType.STRING)
    private AlarmType type;

    public static Alarm ofImmediate(AlarmType type, List<String> recipients, String subject, String content) {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        return Alarm.builder()
                .type(type)
                .recipients(recipients)
                .subject(subject)
                .content(content)
                .sendTime(now.withSecond(0).withNano(0))
                .status(AlarmStatus.SENT)
                .build();
    }

    public static Alarm ofScheduled(AlarmType type, List<String> recipients, String subject, String content, LocalDateTime sendTime) {
        return Alarm.builder()
                .type(type)
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
