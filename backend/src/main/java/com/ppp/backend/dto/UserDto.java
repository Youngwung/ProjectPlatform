package com.ppp.backend.dto;

import com.ppp.backend.dto.skill.BaseSkillDto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseSkillDto{
    Long id;
    String name;
    String email;
    String phoneNumber;
    String experience;
    String password;
    Long providerId;
    String links;
    String providerName;
    //String skills;
}
