package com.poly.dao;

import com.poly.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeDAO extends JpaRepository<Size, Integer> {

}
