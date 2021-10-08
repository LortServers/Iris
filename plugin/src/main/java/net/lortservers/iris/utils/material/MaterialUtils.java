package net.lortservers.iris.utils.material;

import net.lortservers.iris.api.utils.MaterialGroup;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Material utilities.</p>
 */
public final class MaterialUtils {
    private static final Map<String, MaterialGroup<ItemTypeHolder>> itemCache = new HashMap<>();
    private static final Map<String, MaterialGroup<BlockTypeHolder>> blockCache = new HashMap<>();

    /**
     * <p>Checks if the material name has the specified part.</p>
     *
     * @param block the block
     * @param part the material part
     * @return does the block's material name have the part?
     */
    public static boolean hasPart(BlockHolder block, String part) {
        return hasPart(block.getType(), part);
    }

    /**
     * <p>Checks if the material name has the specified part.</p>
     *
     * @param item the item
     * @param part the material part
     * @return does the item's material name have the part?
     */
    public static boolean hasPart(Item item, String part) {
        return hasPart(item.getMaterial(), part);
    }

    public static boolean hasPart(BlockTypeHolder mat, String part) {
        return mat.platformName().toLowerCase(Locale.ROOT).contains(part.toLowerCase(Locale.ROOT));
    }

    public static boolean hasPart(ItemTypeHolder mat, String part) {
        return mat.platformName().toLowerCase(Locale.ROOT).contains(part.toLowerCase(Locale.ROOT));
    }

    public static boolean hasParts(BlockTypeHolder mat, String... parts) {
        return Arrays.stream(parts).anyMatch(e -> hasPart(mat, e));
    }

    public static boolean hasParts(ItemTypeHolder mat, String... parts) {
        return Arrays.stream(parts).anyMatch(e -> hasPart(mat, e));
    }

    private static MaterialGroup<BlockTypeHolder> cacheGetBlock(String... parts) {
        final String part = String.join(" ", parts);
        if (!blockCache.containsKey(part)) {
            blockCache.put(part, BlockMaterialGroup.withCommonPart(parts));
        }
        return blockCache.get(part);
    }

    private static MaterialGroup<ItemTypeHolder> cacheGetItem(String... parts) {
        final String part = String.join(" ", parts);
        if (!itemCache.containsKey(part)) {
            itemCache.put(part, ItemMaterialGroup.withCommonPart(parts));
        }
        return itemCache.get(part);
    }

    public static MaterialGroup<BlockTypeHolder> getGrass() {
        return cacheGetBlock("grass", "fern");
    }

    public static MaterialGroup<BlockTypeHolder> getFlowers() {
        return cacheGetBlock("dandelion", "poppy", "blue_orchid", "allium", "azure_bluet", "tulip", "oxeye_daisy", "red_mushroom", "brown_mushroom", "sunflower", "lilac", "rose", "peony", "cornflower", "lily_of_the_valley", "roots", "fungus", "nether_sprouts", "azalea", "spore_blossom");
    }

    public static MaterialGroup<BlockTypeHolder> getCarpets() {
        return cacheGetBlock("carpet");
    }

    public static MaterialGroup<BlockTypeHolder> getFences() {
        return cacheGetBlock("fence").filter("fence_gate");
    }

    public static MaterialGroup<BlockTypeHolder> getFenceGates() {
        return cacheGetBlock("fence_gate");
    }
}
