package dev.renansouza.document;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

@Controller("/invoice")
@Secured(SecurityRule.IS_AUTHENTICATED)
@SecuritySchemes(
    value = {
        @SecurityScheme(
                type = SecuritySchemeType.HTTP,
                name = "JWT",
                description = "Authentication needed to create an unpack record",
                scheme = "bearer",
                bearerFormat = "jwt"
        ),
        @SecurityScheme(
                type = SecuritySchemeType.HTTP,
                name = "Basic",
                description = "Authentication needed to create an unpack record",
                scheme = "basic"
        )
    }
)
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private String exception = "";

    // TODO remove this after complete BasicAuth and Baerer
    @Hidden
    @Get("/")
    public MutableHttpResponse<String> index() {
        return HttpResponse.<String>status(HttpStatus.OK).body("Index");
    }

    // TODO fix requestBody issue
    @Operation(summary = "Unpack Files",
            description = "Receives a packed zip or gzip file with xml files inside or receives xml files",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Something Went Wrong"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable")
            }
    )
    @Post(value = "/unpack", consumes = MediaType.MULTIPART_FORM_DATA)
    public Single<HttpResponse<String>> upload(StreamingFileUpload file, int flow, int environment) throws IOException {
        return Single.just(new Document(file.getFilename(), environment, flow))
            .flatMap(DocumentValidation::validateDocumentExtension)
            .doOnError(throwable -> {
                log.error("Validation exception: {}", throwable.getMessage());
                exception = throwable.getMessage();
            })
            .doOnSuccess(doc -> {
                log.info("File saved successfuly");
                File tempFile = File.createTempFile(file.getFilename(), "temp");
                file.transferTo(tempFile);
            })
            .map(success -> {
                if (exception != null || !exception.equals("")) {
                    return HttpResponse.<String>status(HttpStatus.CREATED).body("Uploaded");
                } else {
                    return HttpResponse.<String>status(HttpStatus.SERVICE_UNAVAILABLE).body(exception);
                }
            }
        );
    }

}