package com.chishiya.identity_service.service;

import com.chishiya.identity_service.dto.request.UserCreationRequest;
import com.chishiya.identity_service.dto.request.UserUpdateRequest;
import com.chishiya.identity_service.entity.User;
import com.chishiya.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request)
    {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }

    public User updateUser(String userId, UserUpdateRequest request) {
        User user = getUser(userId);
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }

    public void deleteUser(String userId)
    {
        userRepository.deleteById(userId);
    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public User getUser(String id)
    {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


}
