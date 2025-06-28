package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.ETagItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagItemRepository extends JpaRepository<ETagItem, Long> {

    List<ETagItem> findAllByTagType(String tagType);

}
