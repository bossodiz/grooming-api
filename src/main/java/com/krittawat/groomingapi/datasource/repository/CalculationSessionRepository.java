package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EInvoiceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculationSessionRepository extends JpaRepository<EInvoiceSession, Long> {

    Optional<EInvoiceSession> findByInvoiceNo(String invoiceNo);

}
