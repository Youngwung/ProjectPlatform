package com.ppp.backend.dto.alert;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlertPortfolioDto {
    private Long id;
    private Long portfolioId;
    private String status;
    private String content;
    private Timestamp createdAt;
    private boolean isRead;
}
