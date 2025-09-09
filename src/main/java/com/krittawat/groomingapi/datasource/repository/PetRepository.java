package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.entity.EUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<EPet, Long> {

    List<EPet> findByUser(EUserProfile user);

    @Query("SELECT p FROM EPet p WHERE p.user = ?1 AND p.name = ?2")
    List<EPet> findByUserAndName(EUserProfile user, String name);
}