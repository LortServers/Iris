package net.lortservers.iris.utils.material;

import net.lortservers.iris.api.utils.MaterialGroup;
import org.screamingsandals.lib.block.BlockTypeHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record BlockMaterialGroup(List<BlockTypeHolder> materials) implements MaterialGroup<BlockTypeHolder> {
    public static MaterialGroup<BlockTypeHolder> of(BlockTypeHolder... materials) {
        return new BlockMaterialGroup(Arrays.asList(materials));
    }

    public static MaterialGroup<BlockTypeHolder> withCommonPart(String... part) {
        return new BlockMaterialGroup(BlockTypeHolder.all().stream().filter(e -> MaterialUtils.hasParts(e, part)).toList());
    }

    @Override
    public boolean contains(BlockTypeHolder mat) {
        return materials.contains(mat);
    }

    @Override
    public MaterialGroup<BlockTypeHolder> filter(String... parts) {
        return new BlockMaterialGroup(materials.stream().filter(e -> MaterialUtils.hasParts(e, parts)).toList());
    }

    @Override
    public MaterialGroup<BlockTypeHolder> copy() {
        return new BlockMaterialGroup(new ArrayList<>(materials));
    }

    @Override
    public Class<BlockTypeHolder> getMaterialType() {
        return BlockTypeHolder.class;
    }
}
