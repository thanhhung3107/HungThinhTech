package com.poly.controller;

import com.poly.dao.OrderDAO;
import com.poly.dao.OrderDetailDAO;
import com.poly.model.Order;
import com.poly.model.OrderDetail;
import com.poly.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderMyController {
    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    // Danh sách đơn hàng của người dùng đang đăng nhập
    @GetMapping("/ordersmy")
    public String userOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderDAO.findByUserUserId(user.getUserId());
        model.addAttribute("orders", orders);

        return "Ordermy";
    }

    // Chi tiết đơn hàng
    @GetMapping("/ordermy/details/{id}")
    public String orderDetails(@PathVariable("id") int orderID, Model model) {
        List<OrderDetail> orderDetails = orderDetailDAO.findByOrderOrderID(orderID);
        model.addAttribute("orderDetails", orderDetails);
        return "OrdermyDeltail";
    }

}
