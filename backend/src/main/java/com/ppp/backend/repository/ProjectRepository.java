package com.ppp.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.Project;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectSearch {
    List<Project> findByUserId(Long userId);
}
