package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EGroomingReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GroomingReserveRepository extends JpaRepository<EGroomingReserve, Long> {


    @Query("SELECT gr FROM EGroomingReserve gr WHERE gr.reserveDateStart BETWEEN ?1 AND ?2")
    List<EGroomingReserve> findAllByReserveDateStartBetween(LocalDateTime start, LocalDateTime end);

}