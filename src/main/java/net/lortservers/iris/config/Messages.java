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
    @Getter(AccessLevel.NONE)
    @JsonIgnore
    protected static final MiniMessage MINIMESSAGE = MiniMessage.get();
    private String prefix = "<color:gray>(<color:red>!<color:gray>) ";
    private String failedCheck = "<color:red><player> <color:white>failed <color:gold><name> <type> <color:gray>\\ <color:blue>VL: <vl>";
    private String noPermission = "<color:red>No permission!";
    private String invalidCommand = "<color:red>Invalid command!";
    private String alertsToggle = "<color:yellow>Turned alerts <color:green><status><color:yellow>!";

    public Component getMessage(String id) {
        return MINIMESSAGE.parse(prefix + Reflect.getField(this, new String[] {id}));
    }

    public Component getMessage(String id, Map<String, String> placeholders) {
        return MINIMESSAGE.parse(prefix + Reflect.getField(this, new String[] {id}), placeholders);
    }
}
