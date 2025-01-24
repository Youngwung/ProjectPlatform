package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class LinkDto {
    private Long id;
    private Long userId;
    private Long linkTypeId;
    private String url;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
