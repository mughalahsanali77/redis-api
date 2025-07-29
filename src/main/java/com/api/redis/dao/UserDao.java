package com.api.redis.dao;

import com.api.redis.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY = "USER";

    public User save(User user) {
        redisTemplate.opsForHash().put(KEY, user.getId(), user);
        return user;
    }

    public Optional<User> get(String id) {
        return Optional.ofNullable((User) redisTemplate.opsForHash().get(KEY, id));
    }

    public User delete(String id) {
        Optional<User> user = get(id);
        if (user.isPresent()) {
            redisTemplate.opsForHash().delete(KEY, id);
        }
        return user.orElse(null);
    }

    public Map<Object, Object> getAll() {
        return redisTemplate.opsForHash().entries(KEY);
    }

    public User update(User user) {
        redisTemplate.opsForHash().put(KEY, user.getId(), user);
        return user;
    }
}
