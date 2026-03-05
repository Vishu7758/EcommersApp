package com.app.ecom.controller;

import com.app.ecom.entities.User;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/api/users")
    public ResponseEntity<List<User>> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        boolean updated = userService.updateUser(id, updatedUser);
        return updated ? ResponseEntity.ok("User updated successfully") :
                ResponseEntity.notFound().build();
    }
}
