package dev.renansouza;

import dev.renansouza.server.ServerConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ServerConfigurationTest {

    @Test
    public void testServer() {
        ServerConfiguration serverConfiguration = new ServerConfiguration("Server", "flow", "env");
        List<ServerConfiguration> servers = serverConfiguration.getServers();
        Assert.assertEquals(servers.size(), 1);
    }
}