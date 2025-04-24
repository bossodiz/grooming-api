package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EGroomingService;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.repository.GroomingServiceRepository;
import com.krittawat.groomingapi.datasource.repository.PetTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroomingServiceService {
    private final GroomingServiceRepository groomingServiceRepository;
    private final PetTypeRepository petTypeRepository;

    public List<EGroomingService> getAllGroomingServices() {
        return groomingServiceRepository.findAll();
    }

    public List<EGroomingService> getGroomingServiceByPetTypeId(Long petTypeId) {
        Optional<EPetType> petType = petTypeRepository.findById(petTypeId);
        if (petType.isPresent()){
            return groomingServiceRepository.findAllByPetType(petType.get());
        } else {
            return new ArrayList<>();
        }
    }

    public EGroomingService getById(Long id) {
        Optional<EGroomingService> groomingService = groomingServiceRepository.findById(id);
        return groomingService.orElse(null);
    }

}
