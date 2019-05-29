package dev.renansouza;

import dev.renansouza.security.UserCredentials;
import dev.renansouza.security.UserToken;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@MicronautTest(propertySources = "classpath:ssl.yml")
public class DocumentControllerTest {

    @Inject
    private EmbeddedServer server;

    @Test
    public void testInvoiceFailed() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
        Assertions.assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(HttpRequest.GET("/invoice")),"Forbidden");
    }

    @Test
    public void testInvoiceSuccess() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
        final String retrieve = client.toBlocking().retrieve(HttpRequest.GET("/invoice").basicAuth("dev", "dev123"));
        Assertions.assertNotNull(retrieve);
        Assertions.assertEquals("Index", retrieve);
    }

    @Test
    public void testSendZipFile() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));

        final MultipartBody multipartBody = MultipartBody.builder()
                .addPart("file", new File("src/test/resources/logback.zip"))
                .addPart("flow", "1")
                .addPart("environment", "1")
                .build();

        UserToken token = client.toBlocking().retrieve(HttpRequest.POST("/login", new UserCredentials("dev", "dev123")), UserToken.class);
        final HttpResponse<Object> exchange = client.toBlocking().exchange(HttpRequest.POST("/invoice/unpack", multipartBody)
                                                                    .bearerAuth(token.getAccessToken())
                                                                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE));

//        final String retrieve = client.toBlocking().retrieve(HttpRequest.POST("/invoice/unpack", multipartBody)
//                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
//                                        .basicAuth("dev", "dev123"));

        Assertions.assertNotNull(exchange);
        Assertions.assertEquals("Uploaded", exchange);
    }

    @Test
    public void testInvoiceSuccessJWTToken() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
        UserToken token = client.toBlocking().retrieve(HttpRequest.POST("/login", new UserCredentials("dev", "dev123")), UserToken.class);
        final String retrieve = client.toBlocking().retrieve(HttpRequest.GET("/invoice").bearerAuth(token.getAccessToken()));
        Assertions.assertNotNull(retrieve);
        Assertions.assertEquals("Index", retrieve);
    }
}
