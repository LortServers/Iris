package net.lortservers.iris.utils;

import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.item.Item;

import java.util.Locale;

public class MaterialUtils {
    public static boolean hasPart(BlockHolder block, String part) {
        return block.getType().platformName().toLowerCase(Locale.ROOT).contains(part);
    }

    public static boolean hasPart(Item item, String part) {
        return item.getMaterial().platformName().toLowerCase(Locale.ROOT).contains(part);
    }
}
