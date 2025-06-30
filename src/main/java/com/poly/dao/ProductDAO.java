package com.poly.dao;

import com.poly.model.Category;
import com.poly.model.Product;
import com.poly.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

    // Phân trang danh sách sản phẩm
    Page<Product> findAll(Pageable pageable);

    // Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường)
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    // Tìm sản phẩm theo danh mục
    List<Product> findByCategory(Category category);

    Page<Product> findByPriceBetween(double min, double max, Pageable pageable);

    @Query("select new Report(o.category.CategoryName, sum(o.price), count(o)) from Product o group by o.category.CategoryName order by sum(o.price) desc")
    List<Report> getInventoryByCategory();

    @Query("""
              SELECT p FROM Product p 
              JOIN OrderDetail od ON p = od.product 
              GROUP BY p 
              ORDER BY SUM(od.quantity) DESC
            """)
    List<Product> findTopSellingProducts(Pageable pageable);

}
