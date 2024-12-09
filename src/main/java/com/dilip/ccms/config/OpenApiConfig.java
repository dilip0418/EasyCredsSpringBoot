package com.dilip.ccms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Dilip Kumar B K",
                        email = "sudheer0418@gmail.com",
                        url = "https://github.com/dilip0418"
                ),
                title = "EasyCreds - Credit Card Management System",
                version = "1.0.0",
                description = "A secure, user-friendly Credit Card Management application",
                license = @License(
                        name = "Dilip",
                        url = "https://github.com/dilip0418"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Development",
                        url = "http://localhost:8088/api/v1"
                ),
                @Server(
                        description = "Production",
                        url = "https://prod-env/api/v1/" //web address of the prod evn
                ),
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
