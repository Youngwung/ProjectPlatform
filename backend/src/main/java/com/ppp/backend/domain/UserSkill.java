package com.ppp.backend.domain;

import com.ppp.backend.domain.skill.BaseSkill;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserSkill extends BaseSkill{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
