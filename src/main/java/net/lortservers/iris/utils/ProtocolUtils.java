package net.lortservers.iris.utils;

import lombok.*;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.config.Configurator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * <p>Minecraft protocol utilities.</p>
 */
public class ProtocolUtils {
    /**
     * <p>Current Minecraft protocol versions.</p>
     */
    @Getter
    private static @NonNull List<Protocol> protocolVersions;

    static {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://raw.githubusercontent.com/PrismarineJS/minecraft-data/master/data/pc/common/protocolVersions.json"))
                    .GET()
                    .build();
            final String body = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            protocolVersions = Arrays.stream(Configurator.MAPPER.readValue(body, Protocol[].class)).toList();
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
        return protocolVersions.stream().filter(e -> Objects.equals(e.version, version)).findFirst();
    }

    /**
     * <p>Class representing the protocol definition.</p>
     */
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static final class Protocol {
        private String minecraftVersion;
        private int version;
        private int dataVersion;
        private boolean usesNetty;
        private String majorVersion;
    }
}
