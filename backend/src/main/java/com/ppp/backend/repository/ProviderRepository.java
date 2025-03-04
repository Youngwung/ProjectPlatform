package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long>{
	Provider findByName(String name);

}
