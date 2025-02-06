package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppp.backend.domain.UserSkill;

@Repository
public interface UserSkillRepository extends BaseSkillRepository<UserSkill> {
    // 특정 사용자의 기술 목록을 가져오는 메서드
    List<UserSkill> findByUserId(Long userId);

    @Override
    @Query("""
            SELECT u FROM UserSkill u WHERE u.user.id = :ownerId
            """)
    List<UserSkill> findByOwner(@Param("ownerId") Long ownerId);

}