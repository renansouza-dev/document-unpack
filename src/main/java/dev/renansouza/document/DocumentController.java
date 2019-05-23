package dev.renansouza.document;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.security.annotation.Secured;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@Controller("/invoice")
public class DocumentController {

    private final UsernameFetcher usernameFetcher;
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private String exception = "";

    public DocumentController(UsernameFetcher usernameFetcher) {
        this.usernameFetcher = usernameFetcher;
    }

    @Secured("isAuthenticated()")
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    Single<String> index(@Header("Authorization") String authorization) {
        return usernameFetcher.findUsername(authorization);
    }

    //TODO Alter environment and flow to ENUM
    @Post(value = "/unpack", consumes = MediaType.MULTIPART_FORM_DATA)
    @Secured("isAuthenticated()")
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