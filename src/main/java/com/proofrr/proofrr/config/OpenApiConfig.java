package com.proofrr.proofrr.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Proofrr API",
                description = "Authentication, calendar, and meeting endpoints",
                version = "v1"
        ),
        servers = {
                @Server(url = "http://localhost:${server.port}", description = "Local")
        }
)
public class OpenApiConfig {
}
