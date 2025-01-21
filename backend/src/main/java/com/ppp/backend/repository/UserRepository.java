package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.User;
import org.springframework.stereotype.Repository;

import java.nio.file.FileStore;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
