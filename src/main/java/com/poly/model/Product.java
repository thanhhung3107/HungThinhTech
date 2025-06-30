package com.poly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
@ToString(exclude = "category")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId; // Camel case

    @Column(name = "product_name")
    private String productName; // Camel case

    @Column(name = "Description")
    private String description; // Camel case

    @Column(name = "image")
    private String image;

    @Column(name = "Price")
    private Double price;       // Camel case

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


}