package br.com.luzdosbichos.web;

import br.com.luzdosbichos.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        String objectName = storageService.upload(file);
        return ResponseEntity.ok(Map.of(
                "objectName", objectName,
                "message", "Upload concluído com sucesso!"
        ));
    }
}
