package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import com.accenture.codingtest.springbootcodingtest.model.ApiResponse;
import com.accenture.codingtest.springbootcodingtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> usersList = userService.getAllUsers();
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Users has been successfully retrieved",
                usersList
        ), HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable String user_id) {
        User user = userService.getUserById(UUID.fromString(user_id));
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "User has been successfully retrieved",
                user
        ), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.CREATED.value(),
                "User has been successfully created!",
                user), HttpStatus.CREATED);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable String user_id, @RequestBody User user) {
        user.setId(UUID.fromString(user_id));
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "User details has been successfully updated!",
                updatedUser
        ), HttpStatus.OK);
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<?> patchUser(@PathVariable String user_id, @RequestBody Map<String, String> fields) {
        fields.put("id", user_id);
        userService.patchUser(fields);
        fields.remove("id");
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "Fields has been successfully updated!",
                        fields
                )
                , HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable String user_id) {
        userService.deleteUser(UUID.fromString(user_id));
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "User with user_id: '" + user_id + "' has been successfully deleted",
                user_id
        ), HttpStatus.OK);
    }

}
