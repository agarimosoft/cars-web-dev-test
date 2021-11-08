package org.acme.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.fail;

public class WireMockWordsResource implements QuarkusTestResourceConfigurableLifecycleManager {

    private static final String WORD_JSON_FILE = "/words.json";
    private static final String BASE_PATH = "/";
    private static final int WIREMOCK_PORT = 7777;

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(WIREMOCK_PORT);
        wireMockServer.start();
        stubCountries();
        return Collections.singletonMap("quarkus.rest-client.\"org.acme.services.WordsService\".url",
                wireMockServer.baseUrl() + BASE_PATH);
    }

    @Override
    public void stop() {
        if (Objects.nonNull(wireMockServer))
            wireMockServer.stop();
    }

    private void stubCountries() {

        try (InputStream is = WireMockWordsResource.class.getResourceAsStream(WORD_JSON_FILE)) {
            String words = new String(is.readAllBytes());

            wireMockServer.stubFor(get(urlMatching("/words.*"))
                    .willReturn(okJson(words)));

        } catch (IOException e) {
            fail("Could not configure Wiremock server. Caused by: " + e.getMessage());
        }
    }
}
