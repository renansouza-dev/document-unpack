package dev.renansouza.server;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

import java.util.ArrayList;
import java.util.List;

@EachProperty("servers")
public class ServerConfiguration {

    private final String name;
    private final String flow;
    private final String environment;
    private List<ServerConfiguration> servers = new ArrayList<>();

    public ServerConfiguration(@Parameter String name, @Parameter String flow, @Parameter String environment) {
        this.name = name;
        this.flow = flow;
        this.environment = environment;
        servers.add(this);
    }

    public String getName() {
        return name;
    }

    public String getFlow() {
        return flow;
    }

    public String getEnvironment() {
        return environment;
    }

    public List<ServerConfiguration> getServers() {
        return this.servers;
    }

}