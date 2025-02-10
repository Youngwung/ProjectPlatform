package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class AlertPortfolioDto {
    private Long id;
    private Long portfolioId;
    private Long skillId;
    private String status;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
