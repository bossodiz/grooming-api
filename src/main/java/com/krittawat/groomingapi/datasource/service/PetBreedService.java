package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EPetBreed;
import com.krittawat.groomingapi.datasource.repository.PetBreedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetBreedService {
    private final PetBreedRepository petBreedRepository;

    public List<EPetBreed> getAll(){
        return petBreedRepository.findAll();
    }

    public EPetBreed getById(Long id) {
        return petBreedRepository.findById(id).orElse(null);
    }
}
