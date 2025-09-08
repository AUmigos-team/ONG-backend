package br.com.luzdosbichos.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface StorageService {

    String upload(String objectName, InputStream data, String contentType, long contentLength);

    default String upload(MultipartFile file) {
        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try (InputStream is = file.getInputStream()) {
            return upload(objectName, is, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new RuntimeException("Falha ao ler o arquivo para upload", e);
        }
    }
}
