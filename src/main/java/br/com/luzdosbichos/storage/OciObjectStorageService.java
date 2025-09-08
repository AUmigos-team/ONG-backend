package br.com.luzdosbichos.storage;

import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.InputStream;

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
    public String upload(String objectName, InputStream data, String contentType, long contentLength) {
        try {
            PutObjectRequest req = PutObjectRequest.builder()
                    .namespaceName(namespace)
                    .bucketName(bucket)
                    .objectName(objectName)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .putObjectBody(data)
                    .build();

            objectStorage.putObject(req);
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("Falha no upload para OCI Object Storage", e);
        }
    }

}
