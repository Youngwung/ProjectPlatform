package com.ppp.backend.repository;

import com.ppp.backend.domain.Portpolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortpolioRepository extends JpaRepository<Portpolio, Long> {

    // 특정 사용자가 작성한 프로젝트 리스트를 조회
    List<Portpolio> findByUserId(Long userId);
    // 제목 검색
    List<Portpolio> findByTitleContainingIgnoreCase(String title);
}
