package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillDto {
    private Long id;
    private String name;
    private Long skillCategoryId;
    private String description;
}

