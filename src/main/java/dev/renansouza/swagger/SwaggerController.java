package dev.renansouza.swagger;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import io.swagger.v3.oas.annotations.Hidden;

import javax.inject.Inject;

@Hidden
@Controller("/swagger-ui")
public class SwaggerController {

    @Inject
    SwaggerConfig config;

    @View("swagger/index")
    @Get
    public SwaggerConfig index() {
        return config;
    }

}