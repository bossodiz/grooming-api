package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EGroomingService;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroomingServiceRepository extends JpaRepository<EGroomingService, Long> {

    List<EGroomingService> findAllByPetType(EPetType petType);

}