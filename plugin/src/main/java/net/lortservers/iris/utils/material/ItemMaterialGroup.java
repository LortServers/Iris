package net.lortservers.iris.utils.material;

import net.lortservers.iris.utils.MaterialGroup;
import org.screamingsandals.lib.item.ItemTypeHolder;

import java.util.Arrays;
import java.util.List;

public record ItemMaterialGroup(List<ItemTypeHolder> materials) implements MaterialGroup<ItemTypeHolder> {
    public static ItemMaterialGroup of(ItemTypeHolder... materials) {
        return new ItemMaterialGroup(Arrays.asList(materials));
    }

    public static ItemMaterialGroup withCommonPart(String... part) {
        return new ItemMaterialGroup(ItemTypeHolder.all().stream().filter(e -> MaterialUtils.hasParts(e, part)).toList());
    }

    @Override
    public boolean contains(ItemTypeHolder mat) {
        return materials.contains(mat);
    }

    @Override
    public ItemMaterialGroup filter(String... parts) {
        return ItemMaterialGroup.of(materials.stream().filter(e -> MaterialUtils.hasParts(e, parts)).toArray(ItemTypeHolder[]::new));
    }
}
