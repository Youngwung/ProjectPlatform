package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
