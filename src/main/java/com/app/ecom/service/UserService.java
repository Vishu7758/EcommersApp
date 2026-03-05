package com.app.ecom.service;

import com.app.ecom.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private Long idCtr = 0L;
    private final List<User> userList = new ArrayList<>();

    public List<User> getAllUsers() {
        return userList;
    }

    public List<User> addUser(User user) {
        user.setId(++idCtr);
        userList.add(user);
        return userList;
    }

    public Optional<User> getUserById(Long id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public boolean updateUser(Long id, User updatedUser) {
        return getUserById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            return true;
        }).orElse(false);
    }
}
