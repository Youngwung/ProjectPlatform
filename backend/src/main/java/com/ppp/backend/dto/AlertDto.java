package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class AlertDto {
    private Long id;
    private Long joinProjectId;
    private Long skillId;
    private String status;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
