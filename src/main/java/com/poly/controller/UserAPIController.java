package com.poly.controller;

import com.poly.model.User;
import com.poly.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserAPIController {
	 private final UserService userService;

	    @Autowired
	    public UserAPIController(UserService userService) {
	        this.userService = userService;
	    }

	    // API lấy danh sách tất cả người dùng
	    @GetMapping("/getUsers")
	    public List<User> getUsers() {
	        return userService.getAllUsers();
	    }

	    // API lấy thông tin người dùng theo ID
	    @GetMapping("/getUser/{id}")
	    public User getUserById(@PathVariable Integer id) {
	        return userService.getUserById(id);
	    }

	    // API đăng nhập (kiểm tra userName & password)
	    @PostMapping("/login")
	    public User login(@RequestParam String userName, @RequestParam String password) {
	        return userService.authenticateUser(userName, password);
	    }

	    // API lấy thông tin vai trò của người dùng
	    @GetMapping("/getUserInfo")
	    public User getUserInfo(@RequestParam Integer userId) {
	        return userService.getUserById(userId);
	    }
}
