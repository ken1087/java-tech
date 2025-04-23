package com.kang.redis.cache;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
/**
 * Builder를 사용할 때는, 반드시 아래의 어노테이션을 사용
 * @AllArgsConstructor
 * @NoArgsConstructor
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * redis cache 설정
 * value : key name
 * timeToLive : cache 생명 주기
 */
@RedisHash(value = "userCache", timeToLive = 60) 
public class UserCache implements Serializable { // 직렬화 Serializable 사용하기
    
    private static final long serialVersionUID = -21449034499650707L;

    // jakarta사용하면 안됨
    // org.springframework.data.annotation.Id를 사용할 것
    @Id
    private Long id;

    private String username;

    private String name;

    private int age;

}
