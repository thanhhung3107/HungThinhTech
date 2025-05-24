package com.poly.service;

import com.poly.model.User;
import com.poly.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // Lấy thông tin người dùng theo ID
    public User getUserById(Integer userId) {
        Optional<User> user = userDAO.findById(userId);
        return user.orElse(null);
    }

    // Xác thực người dùng khi đăng nhập
    public User authenticateUser(String userName, String password) {
        return userDAO.findByUserNameAndPassword(userName, password);
    }
}
