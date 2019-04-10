package dev.renansouza.document;

class Document {

    private final String filename;
    private final String extension;
    private final int environment;
    private final int flow;

    Document(String filename, int environment, int flow) {
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

    int getEnvironment() {
        return environment;
    }

    int getFlow() {
        return flow;
    }

}
