package com.example.SHOP_APP.services.products;

import com.example.SHOP_APP.dto.ProductDTO;
import com.example.SHOP_APP.entities.Products;
import com.example.SHOP_APP.exception.CustomBusinessException;
import com.example.SHOP_APP.mapper.ProductMapper;
import com.example.SHOP_APP.repository.ProductsRepository;
import com.example.SHOP_APP.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsServiceImp implements ProductsService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductsRepository productsRepository;

    @Override
    public BaseResponse<Void> deleteProducts(Long id) {
        boolean isSuccess = productsRepository.deleteProductById(id) > 0 ? true : false;
        BaseResponse<Void> baseResponse = new BaseResponse<>();
        baseResponse.setSuccess(isSuccess);
        baseResponse.setMessage(isSuccess ? "Product deleted successfully" : "Product not deleted");
        baseResponse.setStatus(isSuccess ? 200 : 400);
        return baseResponse;
    }

    @Override
    public BaseResponse<List<ProductDTO>> geAllProducts() {
        List<Products> list = productsRepository.findAll();
        BaseResponse<List<ProductDTO>> baseResponse = new BaseResponse<>();
        if (!list.isEmpty()) {
            List<ProductDTO> productDTOList = list
                    .stream()
                    .map(productMapper::toProductDTO)
                    .collect(Collectors.toList());
            baseResponse.setData(productDTOList);
            baseResponse.setStatus(200);
            baseResponse.setMessage("Get All Products Success");
            baseResponse.setSuccess(true);
            return baseResponse;
        }else{
            throw new CustomBusinessException("Get All Products Failed");
        }

    }
}
