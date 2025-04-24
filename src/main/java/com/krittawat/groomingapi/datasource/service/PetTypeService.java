package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.repository.PetTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetTypeService {

    private final PetTypeRepository petTypeRepository;

    public List<EPetType> getAll() {
        return petTypeRepository.findAll();
    }

    public EPetType getById(Long id) {
        return petTypeRepository.findById(id).orElse(null);
    }


}
