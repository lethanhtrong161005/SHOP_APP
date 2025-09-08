package com.example.SHOP_APP.repository;

import com.example.SHOP_APP.entities.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface ProductsRepository extends JpaRepository<Products,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Products p WHERE p.id = :id")
    int deleteProductById(Long id);
}
