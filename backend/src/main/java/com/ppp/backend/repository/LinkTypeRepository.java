package com.ppp.backend.repository;

import com.ppp.backend.domain.LinkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkTypeRepository extends JpaRepository<LinkType, Long> {

}
