package br.com.luzdosbichos.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;

@Service
public class S3StorageService implements StorageService {

    private final S3Client s3;
    @Value("${spring.cloud.aws.s3.bucket}") private String bucket;

    public S3StorageService(S3Client s3) {
        this.s3 = s3;
    }

    @Override
    public String upload(String key, InputStream input, String contentType, long size) {
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType != null ? contentType : "application/octet-stream")
                .build();
        s3.putObject(req, RequestBody.fromInputStream(input, size));
        return key;
    }

    @Override
    public void delete(String key) {
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket).key(key).build());
    }

    @Override
    public boolean exists(String key) {
        try {
            s3.headObject(HeadObjectRequest.builder()
                    .bucket(bucket).key(key).build());
            return true;
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404 ||
                    (ex.awsErrorDetails() != null &&
                            "NoSuchKey".equals(ex.awsErrorDetails().errorCode()))) {
                return false;
            }
            throw ex;
        }
    }


}
