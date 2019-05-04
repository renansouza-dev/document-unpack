package dev.renansouza.server;

import io.micronaut.http.annotation.Controller;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.renansouza.Constants.ROOT;

@Controller("/servers")
class ServerService {

    private static final Logger log = LoggerFactory.getLogger(ServerService.class);

    private final List<Server> serverList;

    public ServerService(List<Server> serverList) {
        this.serverList = serverList;
    }

    @EventListener
    void onStartup(ServerStartupEvent event) throws IOException {
        for(Server server: serverList) {
            if (server.getFlow().equals(ServerFlow.BOTH) || server.getFlow().equals(ServerFlow.EMISSION)) {
                final Path emission = Paths.get(ROOT, server.getEnvironment().name().toLowerCase(), ServerFlow.EMISSION.name().toLowerCase());
                if (!Files.exists(emission)) {
                    log.debug("Creating work dir {}", emission);
                    Files.createDirectories(emission);
                }
            }

            if (server.getFlow().equals(ServerFlow.BOTH) || server.getFlow().equals(ServerFlow.RECEIPT)) {
                final Path receipt = Paths.get(ROOT, server.getEnvironment().name().toLowerCase(), ServerFlow.RECEIPT.name().toLowerCase());
                if (!Files.exists(receipt)) {
                    log.debug("Creating work dir {}", receipt);
                    Files.createDirectories(receipt);
                }
            }
        }
    }

    Map<String, Map<String, Long>> queue() throws IOException {
        final Map<String, Map<String, Long>> queue = new HashMap<>();
        for(Server server: serverList) {
            final Map<String, Long> folderList = new HashMap<>();
            Files.list(Paths.get(ROOT, server.getEnvironment().name().toLowerCase())).sorted().forEach(
            folder -> {
                try {
                    final String folderName = folder.getFileName().toString();
                    final long folderCount = Files.list(folder).count();

                    folderList.put(folderName, folderCount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            queue.put(server.getEnvironment().name().toLowerCase(), folderList);
        }
        return queue;
    }

}