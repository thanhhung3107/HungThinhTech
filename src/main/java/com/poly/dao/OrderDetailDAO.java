package com.poly.dao;

import com.poly.model.Order;
import com.poly.model.OrderDetail;
import com.poly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder(Order order);

    List<OrderDetail> findByOrder_User(User user);

    List<OrderDetail> findByOrderIn(List<Order> orders);

    List<OrderDetail> findByOrderOrderID(int orderID);

}
