package com.api.redis.service;

import com.api.redis.dao.UserDao;
import com.api.redis.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public User findById(String id) {
        return userDao.get(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public List<User> findAll() {
        return userDao.getAll()
                .values()
                .stream()
                .map(value -> (User) value)
                .collect(Collectors.toList());
    }

    public User create(User user) {
        // Generate UUID for new user
        String id = UUID.randomUUID().toString();
        user.setId(id);

        // Check for duplicate email (optional enhancement)
        boolean emailExists = userDao.getAll().values().stream()
                .map(obj -> (User) obj)
                .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(user.getEmail()));

        if (emailExists) {
            throw new RuntimeException("A user with this email already exists.");
        }

        return userDao.save(user);
    }

    public User update(User user) {
        if (user.getId() == null || userDao.get(user.getId()).isEmpty()) {
            throw new RuntimeException("Cannot update: User not found with ID: " + user.getId());
        }
        return userDao.update(user);
    }

    public User delete(String id) {
        if (userDao.get(id).isEmpty()) {
            throw new RuntimeException("Cannot delete: User not found with ID: " + id);
        }
        return userDao.delete(id);
    }
}
