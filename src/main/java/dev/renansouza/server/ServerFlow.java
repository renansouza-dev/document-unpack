package dev.renansouza.server;

public enum ServerFlow {

    //TODO use this instead of String
    BOTH(0), EMISSION(1), RECEIPT(2);

    private int value;

    ServerFlow(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}