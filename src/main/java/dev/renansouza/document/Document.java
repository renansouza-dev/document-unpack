package dev.renansouza.document;

class Document {

    private String filename;
    private String extension;
    private DocumentEnvironment environment;
    private DocumentFlow flow;

    Document(String filename, int environment, int flow) {
        this.filename = filename;
        this.extension = filename.substring(filename.lastIndexOf("."));
        this.environment = DocumentEnvironment.valueOf(environment);
        this.flow = DocumentFlow.valueOf(flow);
    }

    String getFilename() {
        return filename;
    }

    String getExtension() {
        return extension;
    }

    DocumentEnvironment getEnvironment() {
        return environment;
    }

    DocumentFlow getFlow() {
        return flow;
    }

}
