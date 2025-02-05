package com.ppp.backend.domain.skill;

import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillLevel;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSkill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "skill_id", nullable = false)
	private Skill skill;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "skill_level_id", nullable = false)
	private SkillLevel skillLevel;

	public void update(Skill skill, SkillLevel skillLevel) {
		this.skill = skill;
		this.skillLevel = skillLevel;
	}
}
