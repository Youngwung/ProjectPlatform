package com.ppp.backend.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectSearch {
    List<Project> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Project p WHERE p.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
