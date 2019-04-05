package dev.renansouza.server;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Controller("/servers")
public class ServerController {

    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

}