package br.com.luzdosbichos.repository.content;

import br.com.luzdosbichos.model.contentItem.ContentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContentItemRepository extends JpaRepository<ContentItem, Long> {
    Optional<ContentItem> findByNamespaceAndKey(String namespace, String key);
}
