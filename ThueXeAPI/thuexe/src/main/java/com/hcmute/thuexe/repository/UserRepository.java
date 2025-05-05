package com.hcmute.thuexe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.thuexe.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}