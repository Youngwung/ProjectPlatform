package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ppp.backend.domain.SkillCategory;

@Repository
public interface SkillCategoryRepository extends JpaRepository<SkillCategory, Long> {}
