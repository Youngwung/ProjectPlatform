package com.ppp.backend.dto;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlertProjectDto {
    private Long id;
    private Long projectId;
    private Long skillId;
    private String status;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
