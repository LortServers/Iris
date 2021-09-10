package net.lortservers.iris.config.lang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Locale;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
public class TranslationContainer {
    @JsonIgnore
    private Locale locale;
    private String prefix;
    private String failedMessage;
}
