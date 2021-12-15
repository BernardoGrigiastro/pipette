package io.octalide.pipette.item;

import io.octalide.pipette.Pipette;
import io.octalide.pipette.block.Blocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    public static final ItemGroup PIPETTE_ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(Pipette.MOD_ID, "pipette_tab"),
            () -> new ItemStack(Blocks.PIPE));

    public static final BlockItem PIPE = new BlockItem(Blocks.PIPE, new Item.Settings().group(PIPETTE_ITEM_GROUP));
    public static final BlockItem PIPE_PULLER = new BlockItem(Blocks.PIPE_PULLER, new Item.Settings().group(PIPETTE_ITEM_GROUP));
    public static final BlockItem PIPE_FILTER = new BlockItem(Blocks.PIPE_FILTER, new Item.Settings().group(PIPETTE_ITEM_GROUP));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(Pipette.MOD_ID, "pipe"), PIPE);
        Registry.register(Registry.ITEM, new Identifier(Pipette.MOD_ID, "pipe_puller"), PIPE_PULLER);
        Registry.register(Registry.ITEM, new Identifier(Pipette.MOD_ID, "pipe_filter"), PIPE_FILTER);
    }
}
