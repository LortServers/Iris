package net.lortservers.iris.utils;

import lombok.Data;

/**
 * <p>Class representing the protocol definition.</p>
 */
@Data
public class Protocol {
    private String minecraftVersion;
    private int version;
    private int dataVersion;
    private boolean usesNetty;
    private String majorVersion;
}
