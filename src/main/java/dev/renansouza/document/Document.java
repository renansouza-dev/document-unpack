package dev.renansouza.document;

class Document {

    private String filename;
    private String extension;
    private int environment;
    private int flow;

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
