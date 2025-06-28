package com.krittawat.groomingapi.datasource.entity;

import com.krittawat.groomingapi.utils.EnumUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag_item")
public class ETagItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private ETag tag;
    @Column(name = "item_id")
    private Long ItemId;
    @Column(name = "tag_item_type")
    private String tagType;
}
