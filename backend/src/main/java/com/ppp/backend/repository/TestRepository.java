package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.entity.Test;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test,Long> {
    List<Test> findByName(String name);
}