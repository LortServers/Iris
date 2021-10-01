package net.lortservers.iris.utils.webhooks;

import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.managers.ConfigurationManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class WebhookDispatcher {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static CompletableFuture<HttpResponse<String>> execute(WebhookExecuteRequest executeRequest) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URL(ConfigurationManager.getInstance().getValue("webhookUrl", String.class).orElseThrow()).toURI())
                        .headers("Content-Type", "application/json;charset=UTF-8")
                        .method("POST", HttpRequest.BodyPublishers.ofString(ConfigurationManagerImpl.MAPPER.writeValueAsString(executeRequest)))
                        .build();

                return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException | URISyntaxException ignored) {
                // ignored
            }
            return null;
        });
    }
}
