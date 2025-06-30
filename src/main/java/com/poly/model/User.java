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
@Table(name = "Users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName; // Sửa thành userName

    @Column(name = "Password")
    private String password; // Sửa thành password

    @Column(name = "full_name")
    private String fullName; // Sửa thành fullName

    @Column(name = "Email")
    private String email; // Sửa thành email

    @Column(name = "Phone")
    private String phone; // Sửa thành phone

    @Column(name = "Address")
    private String address; // Sửa thành address

    @Column(name = "RoleID")
    private Integer roleID; // Sửa thành roleID
}