package com.poly.dao;

import com.poly.model.Cart;
import com.poly.model.Product;
import com.poly.model.Size;
import com.poly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDAO extends JpaRepository<Cart, Integer> {

    Cart findByUserAndProductAndSize(User user, Product product, Size size);

    @Query("SELECT c FROM Cart c JOIN FETCH c.product p JOIN FETCH c.size s WHERE c.user = :user")
    List<Cart> findByUser1(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(c.quantity * p.price), 0) FROM Cart c JOIN c.product p WHERE c.user = :user")
    Double calculateTotalByUser(@Param("user") User user);

    List<Cart> findByUser_UserId(Integer userId);

    List<Cart> findByUser(User user);

    void deleteByUser_UserId(Integer userId);
}
