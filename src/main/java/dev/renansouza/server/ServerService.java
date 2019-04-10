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

@Controller("/servers")
class ServerService {

    private static final Logger log = LoggerFactory.getLogger(ServerService.class);
    private static final String ROOT = "input";

    private final List<Server> serverList;

    public ServerService(List<Server> serverList) {
        this.serverList = serverList;
    }

    @EventListener
    void onStartup(ServerStartupEvent event) throws IOException {
        for(Server server: serverList) {
            if (server.getFlow().equals("both") || server.getFlow().equals("emission")) {
                final Path emission = Paths.get(ROOT, server.getAlias(), "emission");
                if (!Files.exists(emission)) {
                    log.info("Creating work dir {}", emission);
                    Files.createDirectories(emission);
                }
            }

            if (server.getFlow().equals("both") || server.getFlow().equals("receipt")) {
                final Path receipt = Paths.get(ROOT, server.getAlias(), "receipt");
                if (!Files.exists(receipt)) {
                    log.info("Creating work dir {}", receipt);
                    Files.createDirectories(receipt);
                }
            }
        }
    }

    Map<String, Map<String, Long>> queue() throws IOException {
        final Map<String, Map<String, Long>> queue = new HashMap<>();
        for(Server server: serverList) {
            final Map<String, Long> folderList = new HashMap<>();
            Files.list(Paths.get(ROOT, server.getAlias())).sorted().forEach(
            folder -> {
                try {
                    final String folderName = folder.getFileName().toString();
                    final long folderCount = Files.list(folder).count();

                    folderList.put(folderName, folderCount);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            queue.put(server.getAlias(), folderList);
        }
        return queue;
    }

}