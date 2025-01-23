package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Builder
@ToString
public class FindProjectDto {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
