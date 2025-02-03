package com.ppp.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

// @SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class PortfolioServiceTest {
    @Autowired
    private PortfolioService portfolioService;

    // @Test
    public void getOneTest() {
        Long Id = 1L;

        log.info("get = {}", portfolioService.getPortfolioById(Id));
    }
    // @Test
    public void getAllTest() {
        log.info("all={}",portfolioService.getAllPortfolios());
    }

    // @Test
    public void createTest() {
        log.info("create={}",portfolioService.createPortfolio(portfolioService.getPortfolioById(1L)));
    }
}
