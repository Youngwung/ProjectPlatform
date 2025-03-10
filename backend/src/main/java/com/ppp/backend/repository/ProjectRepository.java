package com.ppp.backend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ppp.backend.domain.Project;

import jakarta.transaction.Transactional;


public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectSearch {
    List<Project> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Project p WHERE p.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}
