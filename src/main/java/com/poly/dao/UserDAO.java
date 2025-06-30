package com.poly.dao;

import com.poly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    boolean existsByUserName(String userName);

    User findByUserNameAndPassword(String userName, String password);
}