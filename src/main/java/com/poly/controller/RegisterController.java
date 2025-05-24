package com.poly.controller;

import com.poly.dao.UserDAO;
import com.poly.model.User;
import com.poly.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra tên đăng nhập đã tồn tại chưa
            if (userDAO.existsByUserName(user.getUserName())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Tên người dùng đã tồn tại!");
                return "redirect:/register";
            }

            // Thiết lập Role mặc định là 2 (User)
            user.setRoleID(2); 

            // Lưu user vào database
            userDAO.save(user);

            // Kiểm tra email hợp lệ trước khi gửi
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                // Gửi email xác nhận đăng ký
                String subject = "Chào mừng bạn đến với BrandShop!";
                String content = "<h3>Xin chào, " + user.getFullName() + "!</h3>"
                        + "<p>Cảm ơn bạn đã đăng ký tài khoản tại BrandShop.</p>"
                        + "<p>Hãy sử dụng thông tin đăng nhập để truy cập tài khoản.</p>"
                        + "<p><b>Tên đăng nhập:</b> " + user.getUserName() + "</p>"
                        + "<p>Chúng tôi không hiển thị mật khẩu vì lý do bảo mật.</p>"
                        + "<p>Chúc bạn có trải nghiệm mua sắm tuyệt vời!</p>";

                emailService.sendEmail(user.getEmail(), subject, content);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Kiểm tra email để biết thêm chi tiết.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đăng ký thất bại: " + e.getMessage());
            return "redirect:/register";
        }
    }
}
