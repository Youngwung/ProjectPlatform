package com.ppp.backend.dto;

import java.sql.Timestamp;

import com.ppp.backend.dto.skill.BaseSkillDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDto extends BaseSkillDto {
    private Long id;
    private String userName;
    private String title;
    private String description;
    private String links;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
