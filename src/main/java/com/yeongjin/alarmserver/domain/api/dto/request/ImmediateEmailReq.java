package com.yeongjin.alarmserver.domain.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ImmediateEmailReq {
    @Size(min = 1, message = "at least one recipient must be selected")
    private List<String> recipients;

    @NotEmpty(message = "subject is required")
    private String subject;

    @NotEmpty(message = "content is required")
    private String content;
}
