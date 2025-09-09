package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.datasource.entity.ETag;
import com.krittawat.groomingapi.datasource.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

}
