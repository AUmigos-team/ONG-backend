package br.com.luzdosbichos.config;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class OciConfig {

    @Bean
    public AuthenticationDetailsProvider authProvider() throws IOException {
        ConfigFileReader.ConfigFile config = ConfigFileReader.parseDefault();
        return new ConfigFileAuthenticationDetailsProvider(config);
    }

    @Bean
    public ObjectStorage objectStorageClient(AuthenticationDetailsProvider provider) {
        return ObjectStorageClient.builder()
                .region(Region.fromRegionId("sa-saopaulo-1"))
                .build(provider);
    }
}
