package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.ECalculationSession;
import com.krittawat.groomingapi.datasource.repository.CalculationSessionRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculationSessionService {

    private final CalculationSessionRepository calculationSessionRepository;

    public ECalculationSession getByCalculationId(String calculationId) throws DataNotFoundException {
        return calculationSessionRepository.findByCalculationId(calculationId)
                .orElseThrow(() -> new DataNotFoundException("Calculation session not found for id: " + calculationId));
    }

    public void save(ECalculationSession session) {
        calculationSessionRepository.save(session);
    }


    public ECalculationSession getOrNewByCalculationId(String calcId) {
        return calculationSessionRepository.findByCalculationId(calcId)
                .orElseGet(ECalculationSession::new);
    }
}
