package net.lortservers.iris.utils.material;

import net.lortservers.iris.api.utils.MaterialGroup;
import org.screamingsandals.lib.item.ItemTypeHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ItemMaterialGroup(List<ItemTypeHolder> materials) implements MaterialGroup<ItemTypeHolder> {
    public static MaterialGroup<ItemTypeHolder> of(ItemTypeHolder... materials) {
        return new ItemMaterialGroup(Arrays.asList(materials));
    }

    public static MaterialGroup<ItemTypeHolder> withCommonPart(String... part) {
        return new ItemMaterialGroup(ItemTypeHolder.all().stream().filter(e -> MaterialUtils.hasParts(e, part)).toList());
    }

    @Override
    public boolean contains(ItemTypeHolder mat) {
        return materials.contains(mat);
    }

    @Override
    public MaterialGroup<ItemTypeHolder> filter(String... parts) {
        return new ItemMaterialGroup(materials.stream().filter(e -> MaterialUtils.hasParts(e, parts)).toList());
    }

    @Override
    public MaterialGroup<ItemTypeHolder> copy() {
        return new ItemMaterialGroup(new ArrayList<>(materials));
    }

    @Override
    public Class<ItemTypeHolder> getMaterialType() {
        return ItemTypeHolder.class;
    }
}
