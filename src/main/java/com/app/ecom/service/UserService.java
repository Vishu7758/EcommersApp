package com.app.ecom.service;

import com.app.ecom.dto.UserRequestDTO;
import com.app.ecom.dto.UserResponseDTO;
import com.app.ecom.entities.User;
import com.app.ecom.mapper.UserMapper;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public Optional<UserResponseDTO> fetchUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    public boolean updateUser(Long id, UserResponseDTO updatedUserResponseDTO) {
        return userRepository.findById(id).map(existingUser -> {
            userMapper.updateEntityFromDTO(updatedUserResponseDTO, existingUser);
            userRepository.save(existingUser);
            return true;
        }).orElse(false);
    }
}
