package net.lortservers.iris.utils.material;

import net.lortservers.iris.utils.MaterialGroup;
import org.screamingsandals.lib.block.BlockTypeHolder;

import java.util.Arrays;
import java.util.List;

public record BlockMaterialGroup(List<BlockTypeHolder> materials) implements MaterialGroup<BlockTypeHolder> {
    public static BlockMaterialGroup of(BlockTypeHolder... materials) {
        return new BlockMaterialGroup(Arrays.asList(materials));
    }

    public static BlockMaterialGroup withCommonPart(String... part) {
        return new BlockMaterialGroup(BlockTypeHolder.all().stream().filter(e -> MaterialUtils.hasParts(e, part)).toList());
    }

    @Override
    public boolean contains(BlockTypeHolder mat) {
        return materials.contains(mat);
    }

    @Override
    public BlockMaterialGroup filter(String... parts) {
        return BlockMaterialGroup.of(materials.stream().filter(e -> MaterialUtils.hasParts(e, parts)).toArray(BlockTypeHolder[]::new));
    }
}
