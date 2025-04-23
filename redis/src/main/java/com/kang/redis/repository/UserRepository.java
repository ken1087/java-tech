package com.kang.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kang.redis.entity.User;

/**
 * JPA Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {}