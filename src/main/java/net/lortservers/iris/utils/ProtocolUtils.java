package net.lortservers.iris.utils;

import lombok.Getter;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.config.ConfigurationManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>Minecraft protocol utilities.</p>
 */
public class ProtocolUtils {
    /**
     * <p>Current Minecraft protocol versions.</p>
     */
    @Getter
    private static List<Protocol> protocolVersions;

    public static void updateProtocols() {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://raw.githubusercontent.com/PrismarineJS/minecraft-data/master/data/pc/common/protocolVersions.json"))
                    .GET()
                    .build();
            final String body = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            protocolVersions = ConfigurationManager.MAPPER.readValue(body, ConfigurationManager.MAPPER.getTypeFactory().constructCollectionType(List.class, Protocol.class));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            IrisPlugin.getInstance().getLogger().error("Could not retrieve protocol data!", e);
            protocolVersions = new ArrayList<>();
        }
    }

    /**
     * <p>Gets the protocol definition.</p>
     *
     * @param version the protocol version
     * @return the protocol definition
     */
    public static Optional<Protocol> getProtocol(int version) {
        return protocolVersions.stream().filter(e -> Objects.equals(e.getVersion(), version)).findFirst();
    }
}
