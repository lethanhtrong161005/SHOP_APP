package com.example.SHOP_APP.services.products;

import com.example.SHOP_APP.dto.ProductDTO;
import com.example.SHOP_APP.response.BaseResponse;


import java.util.List;

public interface ProductsService {
    BaseResponse<List<ProductDTO>> geAllProducts();

    BaseResponse<Void> deleteProducts(Long id);
}
