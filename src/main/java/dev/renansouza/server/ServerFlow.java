package dev.renansouza.server;

public enum ServerFlow {

    BOTH(0), EMISSION(1), RECEIPT(2);

    private int value;

    ServerFlow(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ServerFlow getServerFlow(int value){
        for (ServerFlow serverFlow : ServerFlow.values()) {
            if(serverFlow.getValue() == value){
                return serverFlow;
            }
        }
        return null;
    }
}