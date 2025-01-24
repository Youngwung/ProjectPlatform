package com.ppp.backend.service;

import com.ppp.backend.domain.Link;
import com.ppp.backend.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    // 특정 유저의 링크 리스트 반환
    public List<Link> getLinksByUserId(Long userId) {
        return linkRepository.findByUserId(userId);
    }
}