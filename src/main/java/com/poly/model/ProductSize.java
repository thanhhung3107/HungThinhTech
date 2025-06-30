package com.poly.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ProductSizes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_size_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
}
