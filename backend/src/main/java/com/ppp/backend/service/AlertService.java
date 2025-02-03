package com.ppp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ppp.backend.repository.AlertRepository;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;

    // CRUD 메서드 작성
}
