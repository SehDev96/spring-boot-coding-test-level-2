package com.accenture.codingtest.springbootcodingtest.repository;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(@Param("name") String username);

    User findUserByUsername(String username);

    @Query("select u.id from User u where u.username= :username")
    UUID getUserIdByUsername(@Param("username") String username);
}
