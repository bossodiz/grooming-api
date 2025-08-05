package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.repository.ItemsRepository;
import com.krittawat.groomingapi.datasource.repository.PetTypeRepository;
import com.krittawat.groomingapi.datasource.repository.PromotionExcludedItemRepository;
import com.krittawat.groomingapi.datasource.repository.PromotionIncludedItemRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsService {
    private final ItemsRepository itemsRepository;
    private final PetTypeRepository petTypeRepository;
    private final PromotionIncludedItemRepository promotionIncludedItemRepository;
    private final PromotionExcludedItemRepository promotionExcludedItemRepository;

    public List<EItem> getGroomingService() {
        return itemsRepository.findAllByItemType(EnumUtil.ITEM_TYPE.GROOMING);
    }

    public List<EItem> getProducts() {
        return itemsRepository.findAllByItemType(EnumUtil.ITEM_TYPE.PET_SHOP);
    }

    public EItem getByIdOrNew(Long id) {
        if (id == null) {
            return new EItem();
        }
        return itemsRepository.findById(id).orElse(new EItem());
    }

    public void save(EItem item) {
        itemsRepository.save(item);
    }

    public EItem getById(Long id) throws DataNotFoundException {
        return itemsRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Service not found with id: " + id));
    }

    public void deleteById(Long id) throws DataNotFoundException {
        EItem item = getById(id);
        itemsRepository.delete(item);
    }

    public List<EItem> getGroomingServiceByPetType(Long petTypeId) throws DataNotFoundException {
        EPetType petType = petTypeRepository.findById(petTypeId).orElseThrow(()-> new DataNotFoundException("Pet type not found with id: " + petTypeId));
        return itemsRepository.findAllByItemType(EnumUtil.ITEM_TYPE.GROOMING)
                    .stream()
                    .filter(
                            eItem -> eItem.getItemCategory() == EnumUtil.ITEM_CATEGORY.valueOf(petType.getName().toUpperCase()))
                    .toList();
    }

    public List<EItem> getItemsByType(EnumUtil.ITEM_TYPE type) {
        return switch (type) {
            case GROOMING -> getGroomingService();
            case PET_SHOP -> getProducts();
        };
    }

    public int countIncludedByPromotionId(Long id) {
        return promotionIncludedItemRepository.countByPromotionId(id);
    }

    public int countExcludedByPromotionId(Long id) {
        return promotionExcludedItemRepository.countByPromotionId(id);
    }

    public boolean existsIncludedByPromotionIdAndItemId(Long id, Long id1) {
        return promotionIncludedItemRepository.existsByPromotionIdAndItemId(id, id1);
    }

    public boolean existsExcludedByPromotionIdAndItemId(Long id, Long id1) {
        return promotionExcludedItemRepository.existsByPromotionIdAndItemId(id, id1);
    }
}
