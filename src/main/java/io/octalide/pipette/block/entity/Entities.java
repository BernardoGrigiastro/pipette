package io.octalide.pipette.block.entity;

import io.octalide.pipette.Pipette;
import io.octalide.pipette.block.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Entities {
    public static BlockEntityType<PipePullerEntity> PIPE_PULLER;
    public static BlockEntityType<PipeFilterEntity> PIPE_FILTER;

    public static void register() {
        PIPE_PULLER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Pipette.MOD_ID, "pipe_puller"), FabricBlockEntityTypeBuilder.create(PipePullerEntity::new, Blocks.PIPE_PULLER).build(null));
        PIPE_FILTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Pipette.MOD_ID, "pipe_filter"), FabricBlockEntityTypeBuilder.create(PipeFilterEntity::new, Blocks.PIPE_FILTER).build(null));
    }
}
