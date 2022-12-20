package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import com.accenture.codingtest.springbootcodingtest.exceptions.ResourceExistException;
import com.accenture.codingtest.springbootcodingtest.exceptions.ResourceNotFoundException;
import com.accenture.codingtest.springbootcodingtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new ResourceNotFoundException("User", "id", id.toString());
        return user;
    }

    @Override
    public UUID getUserIdByUsername(String username) {
        UUID userId = userRepository.getUserIdByUsername(username);
        if (userId == null) throw new ResourceNotFoundException("User","username",username);
        return userId;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ResourceExistException("Username", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User dbUser = userRepository.findById(user.getId()).orElse(null);
        if (dbUser == null) throw new ResourceNotFoundException("User", "id", user.getId().toString());
        return userRepository.save(user);
    }

    @Override
    public Map<String, String> patchUser(Map<String, String> field) {

        User user = userRepository.getById(UUID.fromString(field.get("id")));

        for (Map.Entry<String, String> entry : field.entrySet()) {
            if (entry.getValue().equals("username")) user.setUsername(field.get("username"));
            if (entry.getValue().equals("password")) user.setPassword(field.get("password"));
        }
        userRepository.save(user);

        return field;
    }

    @Override
    public void deleteUser(UUID id) {
        boolean userExists = userRepository.existsById(id);
        if (!userExists) throw new ResourceNotFoundException("User", "id", id.toString());
        userRepository.deleteById(id);
    }
}
