package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillCategoryDto {
    private Long id;
    private String name;
    private String description;
}
