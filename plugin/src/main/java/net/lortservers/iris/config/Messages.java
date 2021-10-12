package net.lortservers.iris.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Locale;

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
    @JsonIgnore
    private Locale locale = Locale.ENGLISH;
    private String prefix = "[<color:blue>Iris<color:white>]";
    private String failedCheck = "<prefix> <color:gray><player><color:white> failed <color:#ADD8E6><check> <type><color:white> | <color:gray><info><color:white> | <color:#83A9B5>ping: <color:white><ping><color:#45BCE2>ms<color:white>, <color:#83A9B5>loc:<color:white> <loc>, <color:#83A9B5>vl:<color:white> <vl>";
    private String shortFailedCheck = "<prefix> <color:gray><player><color:white> failed <color:#ADD8E6><check> <type><color:white> | <color:#83A9B5>ping: <color:white><ping><color:#45BCE2>ms<color:white>, <color:#83A9B5>loc:<color:white> <loc>, <color:#83A9B5>vl:<color:white> <vl>";
    private String noPermission = "<prefix> <color:red>No permission!";
    private String invalidCommand = "<prefix> <color:red>Invalid command!";
    private String invalidPlayer = "<prefix> <color:red>Invalid player!";
    private String alertsToggle = "<prefix> <color:yellow>Turned alerts <color:green><status><color:yellow>!";
    private String playerInfo = "<prefix> <color:yellow>Connection protocol: <color:gray><protocol>";
    private String judgementDaySet = "<prefix> <color:#FFA500>Set the Judgement Day status for player <color:red><player><color:#FFA500> to <color:red><status><color:#FFA500>.";
    private String judgementDayComplete = "<prefix> <color:#FFA500>Banned <color:red><count><color:#FFA500> players.";
    private String banMessage = "<prefix> <color:red><message>";
    private String banMessageCheating = "Banned for unfair advantage.";
    private String banCommandSuccess = "<prefix> <color:red>Banned <color:white><player><color:red>.";
    private String unbanCommandSuccess = "<prefix> <color:yellow>Cleared the ban status from <color:red><player><color:yellow>.";

    @JsonProperty("locale")
    public String getJSONLocale() {
        return locale.toLanguageTag();
    }

    @JsonProperty("locale")
    public void setJSONLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }
}
