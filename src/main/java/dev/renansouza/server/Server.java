package dev.renansouza.server;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

@EachProperty("server")
class Server {

    private String name;
    private ServerFlow flow;
    private ServerEnvironment environment;

    public Server(@Parameter String id) {
        this.name = id;
    }

    public String getName() {
        return name;
    }

    ServerFlow getFlow() {
        return flow;
    }

    public void setFlow(ServerFlow flow) {
        this.flow = flow;
    }

    ServerEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(ServerEnvironment environment) {
        this.environment = environment;
    }

}