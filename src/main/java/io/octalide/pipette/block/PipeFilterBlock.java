package io.octalide.pipette.block;

import io.octalide.pipette.block.entity.PipeFilterEntity;
import io.octalide.pipette.util.Util;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class PipeFilterBlock extends IPipeBlock {
    public PipeFilterBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(0.5F));
    }

    public Inventory getOutput(World world, BlockState state, BlockPos pos) {
        return Util.getInventoryAt(world, pos.offset(facing(state)));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.PASS;
        }

        NamedScreenHandlerFactory shf = state.createScreenHandlerFactory(world, pos);
        if (shf != null) {
            player.openHandledScreen(shf);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean canConnect(World world, BlockState state, BlockPos pos, BlockState other, Direction dir) {
        if (facing(state) == dir) {
            if (other.getBlock() instanceof IPipeBlock) {
                return false;
            }

            return getOutput(world, state, pos) != null;
        }

        if (other.getBlock() instanceof IPipeBlock pipe) {
            if (other.getBlock() instanceof PipeFilterBlock) {
                return false;
            }

            return true;
        }

        return false;
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
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            ItemScatterer.spawn(world, pos, (PipeFilterEntity) world.getBlockEntity(pos));
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipeFilterEntity(pos, state);
    }
}
