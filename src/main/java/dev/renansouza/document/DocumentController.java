package dev.renansouza.document;

import java.io.File;

import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/invoice")
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    @Get()
    public HttpStatus index() {
        return HttpStatus.OK;
    }

    //TODO Alter environment and flow to ENUM
    @Post(value = "/unpack", consumes = MediaType.MULTIPART_FORM_DATA)
    public Single<HttpResponse<String>> upload(StreamingFileUpload file, int environment, int flow) {
        final StringBuilder exception = new StringBuilder();

        return Single.just(new Document(file.getFilename(), environment, flow))
            .flatMap(DocumentValidation::validateDocumentExtension)
            .doOnError(throwable -> {
                log.error("Validation exception: {}", throwable.getMessage());
                exception.append(throwable.getMessage());
            })
            .doOnSuccess(doc -> {
                log.info("File saved successfuly");
                File tempFile = File.createTempFile(file.getFilename(), "temp");
                file.transferTo(tempFile);
            })
            .map(success -> {
                if (!StringUtils.hasText(exception.toString())) {
                    return HttpResponse.<String>status(HttpStatus.CREATED).body("Uploaded");
                } else {
                    return HttpResponse.<String>status(HttpStatus.CONFLICT).body(exception.toString());
                }
            });
    }

}