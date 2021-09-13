package net.lortservers.iris.utils;

import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.item.Item;

import java.util.Locale;

/**
 * <p>Material utilities.</p>
 */
public class MaterialUtils {
    /**
     * <p>Checks if the material name has the specified part.</p>
     *
     * @param block the block
     * @param part the material part
     * @return does the block's material name have the part?
     */
    public static boolean hasPart(BlockHolder block, String part) {
        return block.getType().platformName().toLowerCase(Locale.ROOT).contains(part);
    }

    /**
     * <p>Checks if the material name has the specified part.</p>
     *
     * @param item the item
     * @param part the material part
     * @return does the item's material name have the part?
     */
    public static boolean hasPart(Item item, String part) {
        return item.getMaterial().platformName().toLowerCase(Locale.ROOT).contains(part);
    }
}
