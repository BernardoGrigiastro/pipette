package io.octalide.pipette.block;

import io.octalide.pipette.block.entity.Entities;
import io.octalide.pipette.block.entity.PipePullerEntity;
import io.octalide.pipette.util.Util;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PipePullerBlock extends IPipeBlock {
    public PipePullerBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(0.5F));
    }

    public Inventory getInput(World world, BlockState state, BlockPos pos) {
        return Util.getInventoryAt(world, pos.offset(facing(state).getOpposite()));
    }

    @Override
    public boolean canConnect(World world, BlockState state, BlockPos pos, BlockState other, Direction dir) {
        if (facing(state).getOpposite() == dir) {
            if (other.getBlock() instanceof IPipeBlock) {
                return false;
            }

            return getInput(world, state, pos) != null;
        }

        if (other.getBlock() instanceof IPipeBlock pipe) {
            if (other.getBlock() instanceof PipePullerBlock) {
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public BlockState buildDefaultState(ItemPlacementContext ctx) {
        BlockState state = getDefaultState();
        state = state.with(FACING, ctx.getSide());

        for (Direction dir : Direction.values()) {
            state = state.with(CONNECTIONS.get(dir), false);
        }

        return state;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipePullerEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Entities.PIPE_PULLER, PipePullerEntity::tick);
    }
}
