package dev.renansouza;

import dev.renansouza.security.UserCredentials;
import dev.renansouza.security.UserToken;
import io.micronaut.http.HttpRequest;
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

    /**
     * Test if the protected URL with no credentials fails with Forbidden
     * @throws MalformedURLException if URL is wrong
     */
    @Test
    public void testInvoiceFailed() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
        Assertions.assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(HttpRequest.GET("/invoice")),"Forbidden");
    }

    /**
     * Test if the protected URL with credentials connects successfully
     * @throws MalformedURLException if URL is wrong
     */
    @Test
    public void testInvoiceSuccess() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
        final String retrieve = client.toBlocking().retrieve(HttpRequest.GET("/invoice").basicAuth("dev", "dev123"));
        Assertions.assertNotNull(retrieve);
        Assertions.assertEquals("Index", retrieve);
    }

    /**
     * Test if send a file with expected extension to a protected URL upload successfully
     * @throws MalformedURLException if URL is wrong
     */
    @Test
    public void testSendZipFile() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));

        final MultipartBody multipartBody = MultipartBody.builder()
                .addPart("file", "logback.zip", new File("src/test/resources/logback.zip"))
                .addPart("flow", "1")
                .addPart("environment", "1")
                .build();

        final String retrieve = client.toBlocking()
                .retrieve(HttpRequest.POST("/invoice/unpack", multipartBody)
                        .basicAuth("dev", "dev123")
                        .contentType(MediaType.MULTIPART_FORM_DATA_TYPE));

        Assertions.assertNotNull(retrieve);
        Assertions.assertEquals("Uploaded", retrieve);
    }

    /**
     * Test if send a file without expected extension to a protected URL upload successfully
     * @throws MalformedURLException if URL is wrong
     */
    @Test
    public void testSendYmlFile() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));

        final MultipartBody multipartBody = MultipartBody.builder()
                .addPart("file", "ssl.yml", new File("src/test/resources/ssl.yml"))
                .addPart("flow", "1")
                .addPart("environment", "1")
                .build();

        Assertions.assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking()
                        .retrieve(HttpRequest.POST("/invoice/unpack", multipartBody)
                                .basicAuth("dev", "dev123")
                                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE)),
                "Extension not supported");
    }

    /**
     * Test if the protected URL with JWT credentials connects successfully
     * @throws MalformedURLException if URL is wrong
     */
    @Test
    public void testInvoiceSuccessJWTToken() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
        UserToken token = client.toBlocking().retrieve(HttpRequest.POST("/login", new UserCredentials("dev", "dev123")), UserToken.class);
        final String retrieve = client.toBlocking().retrieve(HttpRequest.GET("/invoice").bearerAuth(token.getAccessToken()));
        Assertions.assertNotNull(retrieve);
        Assertions.assertEquals("Index", retrieve);
    }

}