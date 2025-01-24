package com.ppp.backend.repository;

import com.ppp.backend.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    // 특정 사용자의 Link 리스트 조회
    List<Link> findByUserId(Long userId);
}
