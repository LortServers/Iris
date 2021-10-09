package net.lortservers.iris.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
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
    protected static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();
    private String prefix = "[<color:blue>Iris<color:white>]";
    private String failedCheck = "<prefix> <color:gray><player><color:white> failed <color:#ADD8E6><check> <type><color:white> | <color:gray><info><color:white> | <color:#83A9B5>ping: <color:white><ping><color:#45BCE2>ms<color:white>, <color:#83A9B5>loc:<color:white> <loc>, <color:#83A9B5>vl:<color:white> <vl>";
    private String shortFailedCheck = "<prefix> <color:gray><player><color:white> failed <color:#ADD8E6><check> <type><color:white> | <color:#83A9B5>ping: <color:white><ping><color:#45BCE2>ms<color:white>, <color:#83A9B5>loc:<color:white> <loc>, <color:#83A9B5>vl:<color:white> <vl>";
    private String noPermission = "<prefix> <color:red>No permission!";
    private String invalidCommand = "<prefix> <color:red>Invalid command!";
    private String alertsToggle = "<prefix> <color:yellow>Turned alerts <color:green><status><color:yellow>!";
    private String playerInfo = "<prefix> <color:yellow>Connection protocol: <color:gray><protocol>";
    private String judgementDaySet = "<prefix> <color:#FFA500>Set the Judgement Day status for player <color:red><player><color:#FFA500> to <color:red><status><color:#FFA500>.";
    private String judgementDayComplete = "<prefix> <color:#FFA500>Banned <color:red><count><color:#FFA500> players.";
    private String banMessage = "<prefix> <color:red><message>";
    private String banMessageCheating = "Banned for unfair advantage.";

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
        return MINIMESSAGE.parse(getRawMessage(id), Maps.newHashMap(ImmutableMap.builder().putAll(placeholders).put("prefix", prefix).build()));
    }

    public String getRawMessage(String id) {
        return (String) Reflect.getField(this, id);
    }
}
