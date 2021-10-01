package net.lortservers.iris.utils.webhooks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.lortservers.iris.utils.webhooks.data.Embed;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class WebhookExecuteRequest {
    @JsonIgnore
    private Long threadId;
    private String content;
    private String username;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @Builder.Default
    private boolean tts = false;
    @Singular
    private List<Embed> embeds;
}
