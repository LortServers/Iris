package net.lortservers.iris.utils.protocol;

import lombok.Getter;
import net.lortservers.iris.IrisPlugin;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.ClassMethod;
import org.screamingsandals.lib.utils.reflect.InstanceMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * <p>Minecraft protocol utilities.</p>
 */
public final class ProtocolUtils {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    
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
            final String body = CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();
            protocolVersions = ConfigurationManagerImpl.MAPPER.readValue(body, ConfigurationManagerImpl.MAPPER.getTypeFactory().constructCollectionType(List.class, Protocol.class));
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

    public static Optional<Protocol> ver2Protocol(String versionString) {
        return protocolVersions.stream().filter(e -> e.getMinecraftVersion().equals(versionString)).findFirst();
    }

    public static Protocol getServerProtocol() {
        return ver2Protocol(Server.getVersion()).orElseThrow();
    }

    public static Protocol getPlayerProtocol(PlayerWrapper player) {
        final InstanceMethod viaMethod = ViaVersionAccessor.getViaApiMethod("getPlayerVersion", UUID.class);
        final ClassMethod psMethod = ProtocolSupportAccessor.getPsApiMethod("getProtocolVersion", SocketAddress.class);
        if (viaMethod != null) {
            return getProtocol((int) viaMethod.invoke(player.getUuid())).orElseThrow();
        }
        if (psMethod != null) {
            final int protocolVersion = (int) Reflect.fastInvoke(psMethod.invokeStatic(player.getChannel().remoteAddress()), "getId");
            if (protocolVersion == -1) {
                return getServerProtocol();
            }
            return getProtocol(protocolVersion).orElseThrow();
        }
        return getServerProtocol();
    }
}
