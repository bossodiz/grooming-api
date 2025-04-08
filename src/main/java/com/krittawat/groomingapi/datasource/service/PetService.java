package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.entity.EPetBreed;
import com.krittawat.groomingapi.datasource.entity.EUser;
import com.krittawat.groomingapi.datasource.repository.PetBreedRepository;
import com.krittawat.groomingapi.datasource.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetBreedRepository petBreedRepository;

    public List<EPet> findByUser(EUser user) {
        return petRepository.findByUser(user);
    }

    public List<EPet> findByUserAndName(EUser user, String name) {
        return petRepository.findByUser(user);
    }

    public EPet insert(EPet pet) {
        return petRepository.save(pet);
    }

    public Optional<EPetBreed> findBreedById(Long id) {
        return petBreedRepository.findById(id);
    }

    public List<EPet> findAll() {
        return petRepository.findAll();
    }

}
