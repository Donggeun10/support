package com.example.support.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityScheme(name = "apiKeyAuth", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "X-API-KEY")
public class OpenApi30Config {
    @Value("${spring.application.name: rest-api local}")
    private String appName;

    @Value("${hostName: localHost}")
    private String hostName;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                      .title(String.format("REST API from %s %s", appName, hostName))
                      .version("1.0")
                      .description("개발 환경용 API 문서입니다."));
    }
}
