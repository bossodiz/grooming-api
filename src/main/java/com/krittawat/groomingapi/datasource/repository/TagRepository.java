package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.ETag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRepository extends JpaRepository<ETag, Long> {

}
