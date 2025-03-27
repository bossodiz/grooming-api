package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<EUser, Long> {
    Optional<EUser> findByUsername(String username);

    Optional<EUser> findByid(Long id);

    boolean existsByUsername(String username);
}
