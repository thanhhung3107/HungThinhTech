package com.poly.controller;

import com.poly.dao.CartDAO;
import com.poly.dao.OrderDAO;
import com.poly.dao.OrderDetailDAO;
import com.poly.model.Cart;
import com.poly.model.Order;
import com.poly.model.OrderDetail;
import com.poly.model.User;
import com.poly.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    CartDAO cartDAO;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderDetailDAO orderDetailDAO;

    @Autowired
    EmailService emailService;

    @PostMapping("/checkout")
    public String checkout(@RequestParam("paymentMethod") String paymentMethod, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartDAO.findByUser(user);
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // ✅ Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(calculateTotal(cartItems));
        order.setStatusId(1); // Đã đặt hàng
        order.setPaymentMethod(paymentMethod);
        orderDAO.save(order);

        // ✅ Thêm sản phẩm vào OrderDetail
        for (Cart cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProduct().getPrice());

            if (cartItem.getSize() != null) {
                orderDetail.setSize(cartItem.getSize());
            } else {
                throw new RuntimeException("Size is missing for product: " + cartItem.getProduct().getProductName());
            }

            orderDetailDAO.save(orderDetail);
            cartDAO.delete(cartItem);
        }

        // ✅ Lưu order vào session để hiển thị trong checkout-success.html
        session.setAttribute("latestOrder", order);

        // ✅ Gửi email xác nhận đơn hàng
        sendOrderConfirmationEmail(user, order);

        return "redirect:/checkout-success";
    }

    private double calculateTotal(List<Cart> cartItems) {
        return cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

    @GetMapping("/checkout-success")
    public String checkoutSuccess(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // ✅ Lấy đơn hàng từ session
        Order latestOrder = (Order) session.getAttribute("latestOrder");
        if (latestOrder == null) {
            return "redirect:/cart";
        }

        // ✅ Lấy danh sách sản phẩm từ OrderDetailDAO
        List<OrderDetail> orderDetails = orderDetailDAO.findByOrder(latestOrder);

        // ✅ Truyền vào Model
        model.addAttribute("order", latestOrder);
        model.addAttribute("cartItems", orderDetails);

        return "checkout-success";
    }

    private void sendOrderConfirmationEmail(User user, Order order) {
        List<OrderDetail> orderDetails = orderDetailDAO.findByOrder(order);
        StringBuilder orderDetailsStr = new StringBuilder();

        for (OrderDetail orderDetail : orderDetails) {
            orderDetailsStr.append("<p>")
                    .append(orderDetail.getProduct().getProductName())
                    .append(" - Số lượng: ").append(orderDetail.getQuantity())
                    .append(" - Giá: ").append(orderDetail.getPrice())
                    .append("</p>");
        }

        String subject = "Xác nhận đơn hàng #" + order.getOrderID();
        String content = "<h3>Xin chào, " + user.getUserName() + "!</h3>"
                + "<p>Bạn đã đặt hàng thành công tại BrandShop.</p>"
                + "<p><b>Mã đơn hàng:</b> #" + order.getOrderID() + "</p>"
                + "<p><b>Phương thức thanh toán:</b> " + order.getPaymentMethod() + "</p>"
                + "<h4>Chi tiết đơn hàng:</h4>" + orderDetailsStr
                + "<p><b>Tổng tiền:</b> " + order.getTotalAmount() + " VND</p>"
                + "<p>Cảm ơn bạn đã mua sắm tại BrandShop!</p>";

        emailService.sendEmail(user.getEmail(), subject, content);
    }
}
