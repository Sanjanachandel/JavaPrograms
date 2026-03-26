package com.capg.rechargenova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.rechargenova.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
