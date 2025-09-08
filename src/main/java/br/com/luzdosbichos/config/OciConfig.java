package br.com.luzdosbichos.config;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class OciConfig {

    private static final Logger log = LoggerFactory.getLogger(OciConfig.class);

    @Value("${oci.config.path:C:/Users/joana/.oci/config}")
    private String ociConfigPath;

    @Value("${oci.config.profile:DEFAULT}")
    private String ociProfile;

    @Value("${oci.objectstorage.region:sa-saopaulo-1}")
    private String region;

    @Bean
    public ObjectStorageClient objectStorageClient() throws Exception {
        Path configPath = Paths.get(ociConfigPath);
        var provider = new ConfigFileAuthenticationDetailsProvider(configPath.toString(), ociProfile);

        // Region do Object Storage
        var ociRegion = Region.fromRegionCode(region);
        ObjectStorageClient client = ObjectStorageClient.builder()
                .region(ociRegion)
                .build(provider);

        log.info("OCI ObjectStorageClient inicializado. config={}, profile={}, region={}",
                configPath, ociProfile, ociRegion.getRegionId());
        return client;
    }
}
