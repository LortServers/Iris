package net.lortservers.iris.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;

/**
 * <p>A class holding the message strings.</p>
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Messages {
    /**
     * <p>The MiniMessage instance.</p>
     */
    @Getter(AccessLevel.NONE)
    @JsonIgnore
    protected static final MiniMessage MINIMESSAGE = MiniMessage.get();
    private String prefix = "<color:gray>(<color:red>!<color:gray>) ";
    private String failedCheck = "<color:red><player> <color:white>failed <color:gold><name> <type> <color:gray>\\ <white><info> <color:gray>\\ <color:blue>VL: <vl>";
    private String shortFailedCheck = "<color:red><player> <color:white>failed <color:gold><name> <type> <color:gray>\\ <color:blue>VL: <vl>";
    private String noPermission = "<color:red>No permission!";
    private String invalidCommand = "<color:red>Invalid command!";
    private String alertsToggle = "<color:yellow>Turned alerts <color:green><status><color:yellow>!";
    private String playerInfo = "<color:yellow>Connection protocol: <color:gray><protocol>";

    /**
     * <p>Gets the message component from id.</p>
     *
     * @param id the message id
     * @return the message component
     */
    public Component getMessage(String id) {
        return getMessage(id, Map.of());
    }

    /**
     * <p>Gets the message component from id and translates placeholders.</p>
     *
     * @param id the message id
     * @param placeholders the message placeholders
     * @return the message component
     */
    public Component getMessage(String id, Map<String, String> placeholders) {
        return MINIMESSAGE.parse(prefix + Reflect.getField(this, id), placeholders);
    }
}
