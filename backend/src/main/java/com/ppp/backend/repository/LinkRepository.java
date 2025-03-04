package com.ppp.backend.repository;

import com.ppp.backend.domain.Link;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    // 특정 사용자의 Link 리스트 조회
    List<Link> findByUserId(Long userId);
    List<Link> findByLinkTypeId(Long linkTypeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Link l WHERE l.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
