package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@ToString
public class LinkDto {
    private Long id;
    private Long userId;
    private String linkTypeName;
    private String url;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
