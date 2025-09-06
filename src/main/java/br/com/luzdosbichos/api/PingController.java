package br.com.luzdosbichos.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class PingController {

    @Value("${APP_BUILD_SHA:dev}")
    private String buildSha;

    @Value("${APP_BUILD_TIME:unknown}")
    private String buildTime;

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "ok", true,
                "service", "aumigos-api",
                "buildSha", buildSha,
                "tetttffet", "teste deploy",
                "buildTime", buildTime
        );
    }
}
