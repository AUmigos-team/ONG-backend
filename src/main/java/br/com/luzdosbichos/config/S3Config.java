package br.com.luzdosbichos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.util.Optional;

@Configuration
public class S3Config {

    private static final Logger log = LoggerFactory.getLogger(S3Config.class);

    @Bean
    public S3Client s3Client() {
        // ENDPOINT
        String endpoint = Optional.ofNullable(System.getenv("AWS_S3_ENDPOINT"))
                .orElseGet(() -> System.getProperty("AWS_S3_ENDPOINT"));
        if (endpoint == null || endpoint.isBlank()) {
            String ns = Optional.ofNullable(System.getenv("S3_NAMESPACE"))
                    .orElseGet(() -> System.getProperty("S3_NAMESPACE"));
            if (ns != null && !ns.isBlank()) {
                endpoint = "https://" + ns + ".compat.objectstorage.sa-saopaulo-1.oraclecloud.com";
            }
        }
        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalStateException("Defina AWS_S3_ENDPOINT ou S3_NAMESPACE.");
        }

        // Captura variáveis/propriedades
        String akEnv = System.getenv("AWS_ACCESS_KEY_ID");
        String akProp = System.getProperty("aws.accessKeyId");
        String skEnv = System.getenv("AWS_SECRET_ACCESS_KEY");
        String skProp = System.getProperty("aws.secretAccessKey");
        String rgEnv = System.getenv("AWS_REGION");
        String rgProp = System.getProperty("aws.region");

        String accessKey = Optional.ofNullable(akEnv).orElse(akProp);
        String secretKey = Optional.ofNullable(skEnv).orElse(skProp);
        String regionStr = Optional.ofNullable(rgEnv).orElse(rgProp);
        if (regionStr == null || regionStr.isBlank()) regionStr = "sa-saopaulo-1";

        // 🔎 Log detalhado (sem expor a chave inteira)
        log.info("S3 config -> endpoint={}, region={}, accessKeyPrefix={}, sources: AK={}, SK={}, RG={}",
                endpoint,
                regionStr,
                accessKey != null ? accessKey.substring(0, Math.min(4, accessKey.length())) : "null",
                akEnv != null ? "ENV" : (akProp != null ? "PROP" : "MISSING"),
                skEnv != null ? "ENV" : (skProp != null ? "PROP" : "MISSING"),
                rgEnv != null ? "ENV" : (rgProp != null ? "PROP" : "DEFAULT"));

        if (accessKey == null || secretKey == null || accessKey.isBlank() || secretKey.isBlank()) {
            throw new IllegalStateException("AWS_ACCESS_KEY_ID e AWS_SECRET_ACCESS_KEY não encontrados.");
        }

        AwsCredentialsProvider creds = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );

        Region region = Region.of(regionStr);

        return S3Client.builder()
                .region(region)
                .credentialsProvider(creds)
                .endpointOverride(URI.create(endpoint))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // necessário na OCI
                        .build())
                .build();
    }
}
