package com.poly.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import com.poly.model.User;
import com.poly.dao.UserDAO;

@Controller
@RequestMapping("/profile")
public class UserProfileController {
    
    private final UserDAO userDAO;

    public UserProfileController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Hiển thị trang thông tin cá nhân
    @GetMapping
    public String userProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Chuyển hướng về trang đăng nhập nếu chưa đăng nhập
        }
        model.addAttribute("user", user);
        return "profile";
    }

    // Cập nhật thông tin cá nhân
    @PostMapping("/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam String address,
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Cập nhật thông tin người dùng
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);

        userDAO.save(user); // Lưu vào database
        session.setAttribute("user", user); // Cập nhật lại session

        model.addAttribute("user", user);
        model.addAttribute("success", "Cập nhật thông tin thành công!");
        return "profile";
    }

    // Đổi mật khẩu (không mã hóa)
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Kiểm tra mật khẩu cũ có đúng không (không mã hóa)
        if (!oldPassword.equals(user.getPassword())) {
            model.addAttribute("error", "Mật khẩu cũ không đúng!");
            return "profile";
        }

        // Cập nhật mật khẩu mới
        user.setPassword(newPassword);
        userDAO.save(user);
        session.setAttribute("user", user);

        model.addAttribute("success", "Đổi mật khẩu thành công!");
        return "profile";
    }
}
