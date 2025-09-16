package br.com.luzdosbichos.model.contentItem;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "content_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"namespace", "key"}))
@Data
public class ContentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String namespace;

    @Column(name = "key", nullable = false)
    private String key;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String data;

    private Integer version = 1;

    private OffsetDateTime updatedAt = OffsetDateTime.now();

}