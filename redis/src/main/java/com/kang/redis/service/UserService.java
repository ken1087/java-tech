package com.kang.redis.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kang.redis.cache.UserCache;
import com.kang.redis.entity.User;
import com.kang.redis.repository.UserRedisRepository;
import com.kang.redis.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {

    /** User Repository */
    private final UserRepository userRepository;

    /** User Redis Repository */
    private final UserRedisRepository userRedisRepository;

    /**
     * get User
     * @param id
     * @return UserCache
     */
    public UserCache getUser(Long id) {
        
        log.info("Start Get User");

        // 1. Cache Server에 ID 값으로 UserCache 취득
        Optional<UserCache> oUserCache = userRedisRepository.findById(id);

        // 2. User Cache값이 존재하는 경우
        if (oUserCache.isPresent()) {

            log.info("Cache Data is existed");

            // 2.1. 반환
            return oUserCache.get();

        } else {

            log.info("Cache Data is not existed");
            
        }

        // 3. 존재하지 않는 경우, DB로부터 데이터 취득
        User user = userRepository.findById(id).get();

        // 4. Cache데이터 작성
        UserCache userCache = UserCache.builder()
            .id(user.getId())
            .username(user.getUsername())
            .name(user.getName())
            .age(user.getAge())
            .build();

        // 5. Cache 데이터 저장
        userRedisRepository.save(userCache);

        // 6. 반환
        return userCache;

    }

    /**
     * post User
     * @param user
     */
    @Transactional
    public void postUser(User user) {

        log.info("Start Post User");
        // 1. User 데이터 저장
        userRepository.save(user);

    }
    
}
