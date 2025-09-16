package br.com.luzdosbichos.service.content;

import br.com.luzdosbichos.model.contentItem.ContentItem;
import br.com.luzdosbichos.repository.content.ContentItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentItemRepository contentItemRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public String getContent(String ns, String key) {
        return contentItemRepository.findByNamespaceAndKey(ns, key)
                .map(ContentItem::getData)
                .orElseThrow(() -> new RuntimeException("Conteúdo não encontrado"));
    }

    public String upsertContent(String ns, String key, String json) {
        ContentItem item = contentItemRepository.findByNamespaceAndKey(ns, key).orElseGet(ContentItem::new);

        if (item.getId() == null) {
            item.setNamespace(ns);
            item.setKey(key);
        }

        item.setData(json);
        item.setVersion(item.getVersion() == null ? 1 : item.getVersion() + 1);
        item.setUpdatedAt(OffsetDateTime.now());

        contentItemRepository.save(item);
        return item.getData();
    }

    public String patchContent(String ns, String key, Map<String, Object> partial) throws Exception {
        ContentItem item = contentItemRepository.findByNamespaceAndKey(ns, key)
                .orElseThrow(() -> new RuntimeException("Conteúdo não encontrado"));

        ObjectNode node = (ObjectNode) mapper.readTree(item.getData());
        partial.forEach((k,v) -> node.set(k, mapper.valueToTree(v)));

        item.setData(mapper.writeValueAsString(node));
        item.setVersion(item.getVersion() + 1);
        item.setUpdatedAt(OffsetDateTime.now());

        contentItemRepository.save(item);
        return item.getData();
    }
}
