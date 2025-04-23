# Redis 공부

## 1. Redis란

- Cache 기술 (자주 사용하는 데이터를 미리 보관해둔 임시 장소)
- Cache는 비교적 저장 공간이 적고 전체적인 비용이 비쌈
- 대신 빠른 IO를 통해 성능적 이점을 가져올 수 있다

## 2. 언제 사용하는가

- 자주 변경이 되지 않는 데이터
- 자주 호출되는 데이터

## 3. Cache의 종류

- 로컬캐시

1. 로컬에서만 사용하는 캐시
2. 외부서버와 트랙잭션 비용이 들지 않기 때문에 속도가 빠름
3. 로컬에서만 사용하기 때문에 분산 서버의 구조에서 캐시를 공유하기 어려움

- 글로벌 캐시

1. 여러서버에서 접근할 수 있는 캐시 서버를 구축하여 사용하는 방식
2. 네트워크를 통해 데이터를 가져오는 트랜잭션 비용이 있기 때문에 로컬캐시에 비해 상대적으로 느림
3. 별도의 서버로 운영되기 때문에 서버 간 데이터 공유에 용이

## 4. Docker에 Redis 띄우기

### 1. Redis image를 받는다.

```bash
docker pull redis
```

### 2. Redis 정보를 설정하면서 실행 [비밀번호 생략 가능]

```bash
docker run -p 6379:6379 --name redis -d redis:latest --requirepass "(비밀번호)"
```

### 3. Redis-Cli 접근

```bash
docker exec -it redis redis-cli -a "비밀번호"
```

### 4. Key 확인

```bash
keys *
```

## 5. 흐름

### 1. User 정보 저장

```java
/**
 * Post User
 * @param user
 */
@PostMapping
public void postUser(@RequestBody User user) {
    userService.postUser(user);
}
```

```java
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
```

### 2. ID로 User 캐시 정보 조회

```java
/**
 * Get UserCache
 * @param id
 * @return UserCache
 */
@GetMapping("/{id}")
public UserCache getUserCache(@PathVariable("id") Long id) {
    return userService.getUser(id);
}
```

```java
// 1. Cache Server에 ID 값으로 UserCache 취득
Optional<UserCache> oUserCache = userRedisRepository.findById(id);
```

### 3. User Cache 정보가 존재하는 경우, Cache 데이터 반환

```java
if (oUserCache.isPresent()) {

    log.info("Cache Data is existed");

    // 2.1. 반환
    return oUserCache.get();

}
```

### 4. User Cache 정보가 존재하지 않는 경우, DB로부터 데이터 취득

```java
// 3. 존재하지 않는 경우, DB로부터 데이터 취득
User user = userRepository.findById(id).get();
```

### 5. DB로부터 취득한 데이터를 Cache 데이터 저장 후 반환

```java
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
```
