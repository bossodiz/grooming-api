package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.repository.PetTypeRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
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

    public EPetType getById(Long id) throws DataNotFoundException {
        return petTypeRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Pet type with id " + id + " not found"));
    }

    public EPetType getByName(String name) throws DataNotFoundException {
        return petTypeRepository.findByName(name).orElseThrow(() -> new DataNotFoundException("Pet type with name " + name + " not found"));
    }


}
