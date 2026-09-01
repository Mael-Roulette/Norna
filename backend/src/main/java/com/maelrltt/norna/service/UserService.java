package com.maelrltt.norna.service;

import com.maelrltt.norna.dto.UpdateUserRequest;
import com.maelrltt.norna.dto.UserRequest;
import com.maelrltt.norna.dto.UserResponse;
import com.maelrltt.norna.entity.User;
import com.maelrltt.norna.exception.UserNotFoundException;
import com.maelrltt.norna.mapper.UserMapper;
import com.maelrltt.norna.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public UserResponse getUserById(Long id) {
        User retrievedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(retrievedUser);
    }

    public UserResponse createUser(UserRequest request) {
        User entityUser = userMapper.toEntity(request);
        User savedUser = userRepository.save(entityUser);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userMapper.updateEntityFromRequest(user, updatedUser);
        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}