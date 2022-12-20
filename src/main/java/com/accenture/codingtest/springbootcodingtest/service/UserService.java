package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(UUID id);

    UUID getUserIdByUsername(String username);

    User createUser(User user);

    User updateUser(User user);

    Map<String,String> patchUser(Map<String,String> field);

    void deleteUser(UUID id);
}
