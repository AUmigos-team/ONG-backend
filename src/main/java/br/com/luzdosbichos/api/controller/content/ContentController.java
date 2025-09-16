package br.com.luzdosbichos.api.controller.content;

import br.com.luzdosbichos.service.content.ContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> get(@PathVariable String ns, @PathVariable String key) {
        try {
            return ResponseEntity.ok(contentService.getContent(ns, key));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @Operation(summary = "Criar ou atualizar conteúdo", description = "Substitui todo o conteúdo do namespace/key")
    @PutMapping("/{ns}/{key}")
    public ResponseEntity<?> upsert(@PathVariable String ns, @PathVariable String key,
                                         @RequestBody Map<String, Object> body) throws JsonProcessingException {
        try {
            String json = new ObjectMapper().writeValueAsString(body);
            return ResponseEntity.ok(contentService.upsertContent(ns, key, json));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @Operation(summary = "Atualizar parcialmente", description = "Atualiza apenas partes do JSON de um namespace/key")
    @PatchMapping("/{ns}/{key}")
    public ResponseEntity<?> patch(@PathVariable String ns, @PathVariable String key,
                                        @RequestBody Map<String, Object> partial) throws Exception {
        try {
            return ResponseEntity.ok(contentService.patchContent(ns, key, partial));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
