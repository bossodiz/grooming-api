package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.ETag;
import com.krittawat.groomingapi.datasource.entity.ETagItem;
import com.krittawat.groomingapi.datasource.repository.TagItemRepository;
import com.krittawat.groomingapi.datasource.repository.TagRepository;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagItemRepository tagItemRepository;


    public List<ETag> getTagsByTagType(EnumUtil.TAG_TYPE tagType) {
        return tagRepository.findAllByTagType(tagType.name());
    }

    public List<ETagItem> getTagItemsByTagType(EnumUtil.TAG_TYPE tagType) {
        return tagItemRepository.findAllByTagType(tagType.name());
    }
}
