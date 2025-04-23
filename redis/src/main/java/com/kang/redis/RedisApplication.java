package com.kang.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Spring boot의 메인 클래스 또는 설정 클래스에에 붙여서 사용하면 되고, 캐싱 기능을 활성화하겠다는 의미를 갖고 있다.
 * 없어도 동작은 한다.
 */
// @EnableCaching
@SpringBootApplication
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

}
