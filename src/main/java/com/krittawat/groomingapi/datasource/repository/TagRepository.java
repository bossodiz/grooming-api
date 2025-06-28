package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.ETag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<ETag, Long> {

    @Query("select DISTINCT t FROM ETag t inner join ETagItem ti on t.id = ti.tag.id where ti.tagType = :tagType")
    List<ETag> findAllByTagType(@Param("tagType") String tagType);
}
