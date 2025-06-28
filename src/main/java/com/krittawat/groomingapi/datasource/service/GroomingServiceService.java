package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EGroomingService;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.repository.GroomingServiceRepository;
import com.krittawat.groomingapi.datasource.repository.PetTypeRepository;
import com.krittawat.groomingapi.datasource.repository.TagItemRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
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

    public List<EGroomingService> getGroomingServicesByPetType(Long petTypeId) {
        return groomingServiceRepository.findAllByPetTypeId(petTypeId);
    }

    public List<EGroomingService> getGroomingServiceByPetTypeId(Long petTypeId) {
        Optional<EPetType> petType = petTypeRepository.findById(petTypeId);
        if (petType.isPresent()){
            return groomingServiceRepository.findAllByPetType(petType.get());
        } else {
            return new ArrayList<>();
        }
    }

    public EGroomingService getById(Long id) throws DataNotFoundException {
        Optional<EGroomingService> groomingService = groomingServiceRepository.findById(id);
        return groomingService.orElseThrow(()-> new DataNotFoundException("Grooming service with id " + id + " not found"));
    }

    public EGroomingService getByIdOrNew(Long id) {
        if (id == null) {
            return new EGroomingService(); // Return a new instance if id is null
        }
        Optional<EGroomingService> groomingService = groomingServiceRepository.findById(id);
        return groomingService.orElse(new EGroomingService());
    }

    public Integer getSequenceByPetTypeId(Long petTypeId) {
        List<EGroomingService> groomingServices = getGroomingServiceByPetTypeId(petTypeId);
        if (groomingServices.isEmpty()) {
            return 1; // If no services exist, start with sequence 1
        } else {
            return groomingServices.stream()
                    .mapToInt(EGroomingService::getSequence)
                    .max()
                    .orElse(0) + 1; // Increment the highest sequence number
        }
    }

    public void save(EGroomingService groomingService) {
        groomingServiceRepository.save(groomingService);
    }

    public void deleteById(Long id) throws DataNotFoundException {
        if (!groomingServiceRepository.existsById(id)) {
            throw new DataNotFoundException("Grooming service with id " + id + " not found");
        }
        groomingServiceRepository.deleteById(id);
    }

}
