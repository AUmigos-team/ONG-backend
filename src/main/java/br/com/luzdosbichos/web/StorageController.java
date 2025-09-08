package br.com.luzdosbichos.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final S3Client s3Client;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        String bucket = Optional.ofNullable(System.getenv("S3_BUCKET"))
                .orElseGet(() -> System.getProperty("S3_BUCKET"));
        if (bucket == null || bucket.isBlank()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Defina S3_BUCKET no ambiente ou passe -DS3_BUCKET=nome-do-bucket");
        }

        String key = "uploads/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha ao ler arquivo: " + e.getMessage());
        }

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(bytes)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha no upload para S3: " + e.getMessage());
        }

        String endpoint = Optional.ofNullable(System.getenv("AWS_S3_ENDPOINT"))
                .orElseGet(() -> System.getProperty("AWS_S3_ENDPOINT"));
        String url = (endpoint != null && !endpoint.isBlank())
                ? endpoint + "/" + bucket + "/" + key
                : ("/" + bucket + "/" + key);

        return ResponseEntity.ok(Map.of(
                "bucket", bucket,
                "key", key,
                "url", url
        ));
    }
}
