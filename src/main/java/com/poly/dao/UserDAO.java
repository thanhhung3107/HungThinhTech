package com.poly.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.User;
@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    boolean existsByUserName(String userName);
    
    User findByUserNameAndPassword(String userName, String password);
}