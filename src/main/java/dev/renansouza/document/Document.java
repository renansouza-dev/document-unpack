package dev.renansouza.document;

import dev.renansouza.server.ServerEnvironment;
import dev.renansouza.server.ServerFlow;

import java.io.File;
import java.nio.file.Paths;

import static dev.renansouza.Constants.ROOT;

class Document {

    private final String filename;
    private final String extension;
    private final ServerEnvironment environment;
    private final ServerFlow flow;

    Document(String filename, ServerEnvironment environment, ServerFlow flow) {
        this.filename = filename;
        this.extension = filename.substring(filename.lastIndexOf("."));
        this.environment = environment;
        this.flow = flow;
    }

    String getFilename() {
        return filename;
    }

    String getExtension() {
        return extension;
    }

    ServerEnvironment getEnvironment() {
        return environment;
    }

    ServerFlow getFlow() {
        return flow;
    }

    File getFile() {
        return Paths.get(ROOT, environment.name().toLowerCase(), flow.name().toLowerCase()).toFile();
    }

}
