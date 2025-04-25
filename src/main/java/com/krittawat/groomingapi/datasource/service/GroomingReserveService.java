package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EGroomingReserve;
import com.krittawat.groomingapi.datasource.repository.GroomingReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroomingReserveService {

    private final GroomingReserveRepository groomingReserveRepository;

    public EGroomingReserve getById(Long id) {
        return groomingReserveRepository.findById(id).orElse(null);
    }

    public List<EGroomingReserve> getAll() {
        return groomingReserveRepository.findAll();
    }

    public EGroomingReserve save(EGroomingReserve eGroomingReserve) {
        return groomingReserveRepository.save(eGroomingReserve);
    }

    public void deleteById(Long id) {
        groomingReserveRepository.deleteById(id);
    }

}
