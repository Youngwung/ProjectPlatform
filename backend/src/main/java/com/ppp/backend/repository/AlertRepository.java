package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ppp.backend.domain.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {}