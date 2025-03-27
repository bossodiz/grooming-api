package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.repository.GroomingServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroomingServiceService {
    private final GroomingServiceRepository groomingServiceRepository;

}
