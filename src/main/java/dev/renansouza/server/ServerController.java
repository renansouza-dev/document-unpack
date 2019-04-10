package dev.renansouza.server;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

@Controller("/server")
class ServerController {

    private static final Logger log = LoggerFactory.getLogger(ServerController.class);
    @Inject
    private ServerService serverService;

    @Get(value = "/queue")
    public HttpResponse<String> queue() {
        try {
            return HttpResponse.<String>status(HttpStatus.OK).body(String.valueOf(serverService.queue()));
        } catch (IOException e) {
            return HttpResponse.<String>status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}