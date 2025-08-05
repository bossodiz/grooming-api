package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.utils.EnumUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemsRepository extends JpaRepository<EItem, Long> {


    List<EItem> findAllByItemType(EnumUtil.ITEM_TYPE itemType);

    Optional<EItem> findById(Long id);

}