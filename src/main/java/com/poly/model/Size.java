package com.poly.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Sizes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private Integer id;

    @Column(name = "size_name", nullable = false, length = 50)
    private String sizeName;
}
