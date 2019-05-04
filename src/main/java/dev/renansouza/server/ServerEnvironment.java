package dev.renansouza.server;

public enum ServerEnvironment {

    TEST(0), PRODUCTION(1);

    private int value;

    ServerEnvironment(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ServerEnvironment getServerEnvironment(int value){
        for (ServerEnvironment serverEnvironment : ServerEnvironment.values()) {
            if(serverEnvironment.getValue() == value){
                return serverEnvironment;
            }
        }
        return null;
    }

}