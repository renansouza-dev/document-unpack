package dev.renansouza.server;

public enum ServerEnvironment {

    //TODO use this instead of String
    TEST(0), PRODUCTION(1);

    private int value;

    ServerEnvironment(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}