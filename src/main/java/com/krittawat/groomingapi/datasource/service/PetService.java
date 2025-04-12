package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.entity.EPetBreed;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.entity.EUser;
import com.krittawat.groomingapi.datasource.repository.PetBreedRepository;
import com.krittawat.groomingapi.datasource.repository.PetRepository;
import com.krittawat.groomingapi.datasource.repository.PetTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetBreedRepository petBreedRepository;
    private final PetTypeRepository petTypeRepository;

    public List<EPet> findByUser(EUser user) {
        return petRepository.findByUser(user);
    }

    public List<EPet> findByUserAndName(EUser user, String name) {
        return petRepository.findByUserAndName(user, name);
    }

    public EPet save(EPet pet) {
        return petRepository.save(pet);
    }

    public Optional<EPetBreed> findBreedById(Long id) {
        return petBreedRepository.findById(id);
    }

    public List<EPet> findAll() {
        return petRepository.findAll();
    }

    public Optional<EPet> findById(Long id) {
        return petRepository.findById(id);
    }

    public Optional<EPetType> findTypeById(Long id) {
        return petTypeRepository.findById(id);
    }


}
