package com.ppp.backend.repository;

import com.ppp.backend.domain.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    // 특정 사용자의 기술 목록을 가져오는 메서드
    List<UserSkill> findByUserId(Long userId);
}