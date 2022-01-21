package com.openpayd.task.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .info(new Info()
                        .title("Exchange Rate API")
                        .version("1.0.0")
                        .description("Swagger for Exchange Rate API")
                        .termsOfService("Terms of service")
                        .license(getLicense())
                        .contact(getContact()));
    }

    public Contact getContact() {
        Contact contact = new Contact();
        contact.setEmail("can.sahintas2324@gmail.com");
        contact.setName("Can Şahintaş");
        contact.setUrl("https://github.com/crymnc");
        contact.setExtensions(Collections.emptyMap());
        return contact;
    }

    public License getLicense() {
        License license = new License();
        license.setName("Apache License, Version 2.0");
        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0");
        license.setExtensions(Collections.emptyMap());
        return license;
    }
}
