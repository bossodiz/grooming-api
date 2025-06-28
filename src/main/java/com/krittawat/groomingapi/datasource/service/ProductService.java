package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EProduct;
import com.krittawat.groomingapi.datasource.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<EProduct> getProducts() {
        return productRepository.findAll();
    }

}
