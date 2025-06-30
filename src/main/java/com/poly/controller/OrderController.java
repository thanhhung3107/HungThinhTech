package com.poly.controller;

import com.poly.dao.*;
import com.poly.model.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CartDAO cartDAO;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    @Autowired
    private ProductSizeDAO productSizeDAO;

    // Hiển thị trang thanh toán
    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        // Lấy thông tin người dùng từ session
        User user = (User) session.getAttribute("user");

        // Nếu chưa đăng nhập, chuyển hướng về trang login
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy giỏ hàng của người dùng
        List<Cart> cartItems = cartDAO.findByUser_UserId(user.getUserId());

        // Kiểm tra giỏ hàng có sản phẩm không
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Nếu giỏ hàng trống, quay về trang giỏ hàng
        }

        Double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("total", total);

        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        return "checkout";
    }

    // Xử lý xác nhận đặt hàng
    @PostMapping("/confirm")
    @Transactional
    public String confirmOrder(
            @RequestParam Long userId,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String paymentMethod,
            RedirectAttributes redirectAttributes) {

        // Chuyển đổi userId từ Long sang Integer
        Integer userIdInt = (Integer) userId.intValue();

        // Tìm user từ database
        User user = userDAO.findById(userIdInt).orElse(null);

        // Kiểm tra nếu user không tồn tại
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không hợp lệ!");
            return "redirect:/cart";
        }

        // Lấy danh sách sản phẩm trong giỏ hàng
        List<Cart> cartItems = cartDAO.findByUser(user);
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống!");
            return "redirect:/cart";
        }

        // Cập nhật thông tin người dùng nếu có thay đổi
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        userDAO.save(user);

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date()); // Lấy ngày hiện tại
        order.setTotalAmount(Double.valueOf(cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum()));
        order.setStatusId(Integer.valueOf(1)); // Trạng thái mặc định "Chờ xử lý"
        order.setPaymentMethod(paymentMethod);
        orderDAO.save(order);

        // Lưu chi tiết đơn hàng và cập nhật tồn kho
        for (Cart item : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetail.setSize(item.getSize());
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setPrice(item.getProduct().getPrice());
            orderDetailDAO.save(orderDetail);

            // Cập nhật số lượng tồn kho
            ProductSize productSize = productSizeDAO.findByProductAndSize(item.getProduct(), item.getSize());
            if (productSize != null && productSize.getStockQuantity() >= item.getQuantity()) {
                productSize.setStockQuantity(Integer.valueOf(productSize.getStockQuantity() - item.getQuantity()));
                productSizeDAO.save(productSize);
            } else {
                redirectAttributes.addFlashAttribute("error", "Sản phẩm " + item.getProduct().getProductName() + " đã hết hàng!");
                return "redirect:/cart";
            }
        }

        // Xóa giỏ hàng sau khi đặt hàng thành công

        cartDAO.deleteByUser_UserId(userIdInt);

        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
        return "redirect:/order/success";
    }

    // Trang thông báo đặt hàng thành công

    @GetMapping("/success")
    public String orderSuccess() {
        return "success";
    }
}
