package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class FindProjectDto {
    private Long id;
    private String title;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
