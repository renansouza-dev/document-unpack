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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

@Controller("/invoice")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private String exception = "";

    @Get("/")
    public MutableHttpResponse<String> index() {
        return HttpResponse.<String>status(HttpStatus.OK).body("Index");
    }

    //TODO Alter environment and flow to ENUM
    @Operation(summary = "Greets a person",
            description = "A friendly greeting is returned"
    )
    @ApiResponse(
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type="string"))
    )
    @ApiResponse(responseCode = "400", description = "Invalid Name Supplied")
    @ApiResponse(responseCode = "404", description = "Person not found")
    @Tag(name = "greeting")
    @Post(value = "/unpack", consumes = MediaType.MULTIPART_FORM_DATA)
    public Single<HttpResponse<String>> upload(StreamingFileUpload file, int environment, int flow) throws IOException {
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
                    return HttpResponse.<String>status(HttpStatus.CONFLICT).body(exception);
                }
            });
    }

}