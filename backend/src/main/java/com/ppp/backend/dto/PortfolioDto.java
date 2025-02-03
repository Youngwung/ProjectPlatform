package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Builder(toBuilder = true)
@ToString
public class PortfolioDto {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String links;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
