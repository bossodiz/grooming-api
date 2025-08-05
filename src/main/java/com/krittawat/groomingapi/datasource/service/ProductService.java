package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.repository.ItemsRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ItemsRepository productRepository;

    public List<EItem> getProducts() {
        return productRepository.findAll();
    }

    public EItem getProductsById(Long id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(()->new DataNotFoundException("Product not found"));
    }

}
