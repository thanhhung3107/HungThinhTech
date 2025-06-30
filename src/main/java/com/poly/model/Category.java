package com.poly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categories")
@ToString(exclude = "products")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name")
    private String CategoryName;

    @OneToMany(mappedBy = "category") // "category" là tên trường trong class Product
    private List<Product> products; // Danh sách các sản phẩm thuộc danh mục này


}
