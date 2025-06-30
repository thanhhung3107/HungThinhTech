package com.poly.controller;

import com.poly.dao.OrderDAO;
import com.poly.dao.OrderDetailDAO;
import com.poly.enums.OrderStatus;
import com.poly.model.Order;
import com.poly.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderManageController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderDAO.findAllWithUsers();
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer orderId, Model model) {
        Order order = orderDAO.findByIdWithUser(orderId);
        if (order == null) {
            return "redirect:/orders";
        }

        List<OrderDetail> orderDetails = orderDetailDAO.findByOrder(order);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/details";
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam Integer orderId, @RequestParam Integer statusId) {
        Order order = orderDAO.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatusId(statusId);
            orderDAO.save(order);
        }
        return "redirect:/orders/" + orderId;
    }
}
