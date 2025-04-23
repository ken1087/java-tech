package com.kang.redis.repository;

import org.springframework.data.repository.CrudRepository;

import com.kang.redis.cache.UserCache;

/**
 * Cache 저장용 Repository
 * CrudRepository를 상속해야 한다.
 */
public interface UserRedisRepository extends CrudRepository<UserCache, Long>{}
