package com.example.SHOP_APP.controllers;

import com.example.SHOP_APP.response.BaseResponse;
import com.example.SHOP_APP.services.products.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @GetMapping("/all")
    public ResponseEntity<?> getProducts() {
        BaseResponse baseResponse = productsService.geAllProducts();
        return ResponseEntity.status(baseResponse.getStatus()).body(baseResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProducts(@PathVariable Long id) {
        BaseResponse<Void> baseResponse = productsService.deleteProducts(id);
        return ResponseEntity.status(baseResponse.getStatus()).body(baseResponse);
    }
}
