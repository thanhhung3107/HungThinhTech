package com.poly.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Order implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderID;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading để tối ưu hiệu suất
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "StatusID", nullable = false)
    private Integer statusId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @PrePersist
    protected void onCreate() {
        orderDate = new Date(); // Tự động cập nhật ngày đặt hàng khi tạo Order
    }
}
