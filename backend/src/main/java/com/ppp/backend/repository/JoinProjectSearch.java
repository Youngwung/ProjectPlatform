package com.ppp.backend.repository;

import org.springframework.data.domain.Page;

import com.ppp.backend.domain.JoinProject;

public interface JoinProjectSearch {
	Page<JoinProject> search1();
}