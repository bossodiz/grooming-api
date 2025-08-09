package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EInvoiceSession;
import com.krittawat.groomingapi.datasource.repository.CalculationSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculationSessionService {

    private final CalculationSessionRepository calculationSessionRepository;

    public void save(EInvoiceSession session) {
        calculationSessionRepository.save(session);
    }


    public EInvoiceSession getOrNewByInvoiceNo(String invoiceNo) {
        return calculationSessionRepository.findByInvoiceNo(invoiceNo)
                .orElseGet(EInvoiceSession::new);
    }

    public void delete(EInvoiceSession session) {
        calculationSessionRepository.delete(session);
    }
}
