package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.ECalculationSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculationSessionRepository extends JpaRepository<ECalculationSession, Long> {

    Optional<ECalculationSession> findByCalculationId(String calculationId);

}
