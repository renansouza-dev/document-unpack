package dev.renansouza.document;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.renansouza.server.ServerEnvironment.getServerEnvironment;
import static dev.renansouza.server.ServerFlow.getServerFlow;

@Controller("/invoice")
class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
    private String exception = "";

    @Post(value = "/unpack", consumes = MediaType.MULTIPART_FORM_DATA)
    public Single<HttpResponse<String>> upload(StreamingFileUpload file, int environment, int flow) {
        return Single.just(new Document(file.getFilename(), getServerEnvironment(environment), getServerFlow(flow)))
            .flatMap(DocumentValidation::validateDocumentExtension)
            .doOnError(throwable -> {
                log.error("Validation exception: {}", throwable.getMessage());
                exception = throwable.getMessage();
            })
            .doOnSuccess(doc -> {
                log.info("File saved successfully");
                //TODO implementar o salvar arquivo
            })
            .map(success -> {
                if (exception != null && !exception.equals("")) {
                    return HttpResponse.<String>status(HttpStatus.CREATED).body("Uploaded");
                } else {
                    return HttpResponse.<String>status(HttpStatus.CONFLICT).body(exception);
                }
            });
    }

}