package io.octalide.pipette.block;

import io.octalide.pipette.Pipette;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {
    public static final Block PIPE = new PipeBasicBlock();
    public static final Block PIPE_PULLER = new PipePullerBlock();
    public static final Block PIPE_FILTER = new PipeFilterBlock();

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(Pipette.MOD_ID, "pipe"), PIPE);
        Registry.register(Registry.BLOCK, new Identifier(Pipette.MOD_ID, "pipe_puller"), PIPE_PULLER);
        Registry.register(Registry.BLOCK, new Identifier(Pipette.MOD_ID, "pipe_filter"), PIPE_FILTER);
    }
}
