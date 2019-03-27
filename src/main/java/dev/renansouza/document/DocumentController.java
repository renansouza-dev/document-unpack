package dev.renansouza.document;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
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

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    DocumentService invoiceService;
    String exception = "";

    @Inject
    DocumentController(DocumentService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Get("/")
    public HttpStatus index() {
        return HttpStatus.OK;
    }

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