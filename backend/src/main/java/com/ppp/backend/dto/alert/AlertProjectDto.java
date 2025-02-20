package com.ppp.backend.dto.alert;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlertProjectDto {
    private Long id;
    private Long projectId;
    private String status;
    private String content;
    private Timestamp createdAt;
    private boolean isRead;
}
