package br.com.luzdosbichos.storage;

import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class OciObjectStorageService implements StorageService {

    private final ObjectStorage objectStorage;

    @Value("${oci.objectstorage.namespace}")
    private String namespace;

    @Value("${oci.objectstorage.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest req = PutObjectRequest.builder()
                    .namespaceName(namespace)
                    .bucketName(bucket)
                    .objectName(objectName)
                    .contentLength(file.getSize())
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse resp = objectStorage.putObject(req, is);
            // Retorne a URL pública (se o bucket for público) ou apenas o nome do objeto
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("Falha no upload para OCI Object Storage", e);
        }
    }
}
