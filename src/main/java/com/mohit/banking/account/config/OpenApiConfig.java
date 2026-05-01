package com.mohit.banking.account.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfig - SpringDoc OpenAPI configuration for Banking Account Service.
 * @author mohit
 */
@Configuration
public class OpenApiConfig {

    private static final Logger LOG = LoggerFactory.getLogger(OpenApiConfig.class);

    /**
     * Creates the OpenAPI bean with API metadata.
     * @return OpenAPI instance
     */
    @Bean
    public OpenAPI bankingOpenAPI() {
        LOG.info("OpenApiConfig:: bankingOpenAPI method started");
        return new OpenAPI()
                .info(new Info()
                        .title("Banking Account Service API")
                        .description("Kafka event-driven banking account management service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mohit")
                                .email("mohit@banking.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
