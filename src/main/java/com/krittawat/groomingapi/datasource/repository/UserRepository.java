package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.ERole;
import com.krittawat.groomingapi.datasource.entity.EUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<EUserProfile, Long> {
    Optional<EUserProfile> findByUsername(String username);

    Optional<EUserProfile> findByid(Long id);

    boolean existsByUsername(String username);

    List<EUserProfile> findByRole(ERole role);

    Optional<EUserProfile> findByUsernameAndIdNot(String username, Long id);
}
