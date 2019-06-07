package dev.renansouza;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@OpenAPIDefinition(
    info = @Info(
            title = "document-unpack",
            version = "0.1",
            description = "Document Unpack API",
            license = @License(name = "MIT", url = "https://github.com/renansouza-dev/document-unpack/blob/master/LICENSE"),
            contact = @Contact(url = "https://renansouza-dev.github.io", name = "Renan Souza", email = "renansouza-dev@github.io")
    ),
    security = {
            @SecurityRequirement(name = "Base Auth"),
            @SecurityRequirement(name = "JWT")
    }
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

}