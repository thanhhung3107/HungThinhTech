package com.poly.dao;

import java.util.Date;
import java.util.List;

import com.poly.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueStatDAO extends JpaRepository<Order, Integer> {

    // 1. Doanh thu theo ngày
    @Query(value = """
        SELECT 
            CONVERT(date, order_date) AS order_day,
            SUM(total_amount) AS revenue
        FROM orders
        WHERE (:fromDate IS NULL OR order_date >= :fromDate)
          AND (:toDate IS NULL OR order_date <= :toDate)
        GROUP BY CONVERT(date, order_date)
        ORDER BY order_day
        """, nativeQuery = true)
    List<Object[]> sumByDay(
            @Param("fromDate") Date from,
            @Param("toDate") Date to
    );

    // 2. Doanh thu theo tháng
    @Query("""
        SELECT YEAR(o.orderDate), MONTH(o.orderDate), SUM(o.totalAmount)
        FROM Order o
        WHERE (:from IS NULL OR o.orderDate >= :from)
          AND (:to   IS NULL OR o.orderDate <= :to)
        GROUP BY YEAR(o.orderDate), MONTH(o.orderDate)
        ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)
    """)
    List<Object[]> sumByMonth(@Param("from") Date from,
                              @Param("to") Date to);

    // 3. Doanh thu theo năm
    @Query("""
        SELECT YEAR(o.orderDate), SUM(o.totalAmount)
        FROM Order o
        WHERE (:from IS NULL OR o.orderDate >= :from)
          AND (:to   IS NULL OR o.orderDate <= :to)
        GROUP BY YEAR(o.orderDate)
        ORDER BY YEAR(o.orderDate)
    """)
    List<Object[]> sumByYear(@Param("from") Date from,
                             @Param("to") Date to);

    // 4. Doanh thu theo sản phẩm
    @Query("""
        SELECT p.productId, p.productName, SUM(d.quantity * d.price)
        FROM OrderDetail d
        JOIN d.product p
        GROUP BY p.productId, p.productName
        ORDER BY SUM(d.quantity * d.price) DESC
    """)
    List<Object[]> sumByProduct();

    // 5. Doanh thu theo danh mục
    @Query("""
        SELECT c.categoryId, c.CategoryName, SUM(d.quantity * d.price)
        FROM OrderDetail d
        JOIN d.product p
        JOIN p.category c
        GROUP BY c.categoryId, c.CategoryName
        ORDER BY SUM(d.quantity * d.price) DESC
    """)
    List<Object[]> sumByCategory();

    // 6. Doanh thu theo phương thức thanh toán
    @Query("""
        SELECT o.paymentMethod, SUM(o.totalAmount)
        FROM Order o
        GROUP BY o.paymentMethod
    """)
    List<Object[]> sumByPayment();

    // 7. Doanh thu theo người dùng
    @Query("""
        SELECT u.userId, u.fullName, SUM(o.totalAmount)
        FROM Order o
        JOIN o.user u
        GROUP BY u.userId, u.fullName
        ORDER BY SUM(o.totalAmount) DESC
    """)
    List<Object[]> sumByUser();
}
