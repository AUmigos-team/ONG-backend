package br.com.luzdosbichos.api.controller.content;

import br.com.luzdosbichos.service.content.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @Operation(summary = "Buscar conteúdo", description = "Retorna o JSON de um namespace e key específicos")
    @GetMapping("/{ns}/{key}")
    public ResponseEntity<String> get(@PathVariable String ns, @PathVariable String key) {
        return ResponseEntity.ok(contentService.getContent(ns, key));
    }

    @Operation(summary = "Criar ou atualizar conteúdo", description = "Substitui todo o conteúdo do namespace/key")
    @PutMapping("/{ns}/{key}")
    public ResponseEntity<String> upsert(@PathVariable String ns, @PathVariable String key,
                                         @RequestBody String json) {
        return ResponseEntity.ok(contentService.upsertContent(ns, key, json));
    }

    @Operation(summary = "Atualizar parcialmente", description = "Atualiza apenas partes do JSON de um namespace/key")
    @PatchMapping("/{ns}/{key}")
    public ResponseEntity<String> patch(@PathVariable String ns, @PathVariable String key,
                                        @RequestBody Map<String, Object> partial) throws Exception {
        return ResponseEntity.ok(contentService.patchContent(ns, key, partial));
    }
}
