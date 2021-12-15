package io.octalide.pipette.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PipeBasicBlock extends IPipeBlock {
    public PipeBasicBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(0.5F));
    }

    @Override
    public boolean canConnect(World world, BlockState state, BlockPos pos, BlockState other, Direction dir) {
        return other.getBlock() instanceof IPipeBlock pipe;
    }

    @Override
    public BlockState buildDefaultState(ItemPlacementContext ctx) {
        BlockState state = getDefaultState();
        state = state.with(FACING, ctx.getSide().getOpposite());

        for (Direction dir : Direction.values()) {
            state = state.with(CONNECTIONS.get(dir), false);
        }

        return state;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
