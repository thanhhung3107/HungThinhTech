package com.poly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderDetails")
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detailid")
    private Integer orderDetailID;

    @ManyToOne
    @JoinColumn(name = "orderID") // Consistent naming
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id") // Consistent naming
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size; // Thêm quan hệ với Size

    @Column(name = "quantity") // Consistent naming
    private Integer quantity;

    @Column(name = "price") // Consistent naming
    private Double price;
}
