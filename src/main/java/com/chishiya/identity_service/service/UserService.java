package com.chishiya.identity_service.service;

import com.chishiya.identity_service.dto.request.UserCreationRequest;
import com.chishiya.identity_service.dto.request.UserUpdateRequest;
import com.chishiya.identity_service.dto.response.UserResponse;
import com.chishiya.identity_service.entity.User;
import com.chishiya.identity_service.enums.Role;
import com.chishiya.identity_service.exception.AppException;
import com.chishiya.identity_service.exception.ErrorCode;
import com.chishiya.identity_service.mapper.UserMapper;
import com.chishiya.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request)
    {
        if(userRepository.existsByUsername(request.getUsername()))
        {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);
        
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId)
    {
        userRepository.deleteById(userId);
    }

    public List<UserResponse> getUsers()
    {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(String id)
    {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }
}
