package com.kang.redis.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kang.redis.cache.UserCache;
import com.kang.redis.entity.User;
import com.kang.redis.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    
    /**
     * Get UserCache
     * @param id
     * @return UserCache
     */
    @GetMapping("/{id}")
    public UserCache getUserCache(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    /**
     * Post User
     * @param user
     */
    @PostMapping
    public void postUser(@RequestBody User user) {
        userService.postUser(user);
    }
    
}
