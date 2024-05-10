package com.yeongjin.alarmserver.domain.api.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Comment("메일")
public class EmailAlarm {
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

    @Comment("발송 여부")
    private boolean isSent;

    public static EmailAlarm ofImmediate(List<String> recipients, String subject, String content) {
        return EmailAlarm.builder()
                .recipients(recipients)
                .subject(subject)
                .content(content)
                .sendTime(LocalDateTime.now())
                .isSent(true)
                .build();
    }

    public static EmailAlarm ofScheduled(List<String> recipients, String subject, String content, LocalDateTime sendTime) {
        return EmailAlarm.builder()
                .recipients(recipients)
                .subject(subject)
                .content(content)
                .sendTime(sendTime.withSecond(0))
                .isSent(false)
                .build();
    }

    public void setSent() {
        this.isSent = true;
    }
}
