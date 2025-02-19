package com.ppp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
    private Long id;
    private Long userId;
    private Long linkTypeId;
    private String url;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
