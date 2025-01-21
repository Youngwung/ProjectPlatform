package com.ppp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindProjectDto {
    private Long id; // FindProject의 ID
    private Long userId; // User의 ID
    private String title; // 프로젝트 제목
    private String description; // 프로젝트 설명
}