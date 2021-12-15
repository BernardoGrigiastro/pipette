package io.octalide.pipette.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public abstract class IPipeBlock extends BlockWithEntity {
    public static final VoxelShape CORE_SHAPE = Block.createCuboidShape(1, 1, 1, 15, 15, 15);
    public static final DirectionProperty FACING = DirectionProperty.of("facing");
    public static final Map<Direction, BooleanProperty> CONNECTIONS = new HashMap<>();

    public IPipeBlock(Settings settings) {
        super(settings);
    }
    
    public Direction facing(BlockState state) {
        return state.get(FACING);
    }

    public abstract boolean canConnect(World world, BlockState state, BlockPos pos, BlockState other, Direction dir);
    public abstract BlockState buildDefaultState(ItemPlacementContext ctx);
    public abstract BlockEntity createBlockEntity(BlockPos pos, BlockState state);

    public ArrayList<Direction> connections(BlockState state) {
        ArrayList<Direction> connections = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            if (hasConnection(state, dir)) {
                connections.add(dir);
            }
        }

        return connections;
    }

    public boolean hasConnection(BlockState state, Direction dir) {
        return state.get(CONNECTIONS.get(dir));
    }

    public BlockState setConnection(BlockState state, Direction dir, boolean value) {
        return state.with(CONNECTIONS.get(dir), value);
    }

    public void updateConnections(World world, BlockState state, BlockPos pos) {
        if (world.isClient()) {
            return;
        }

        for (Direction dir : Direction.values()) {
            BlockPos offset = pos.offset(dir);
            BlockState other = world.getBlockState(offset);

            state = setConnection(state, dir, canConnect(world, state, pos, other, dir));

            world.setBlockState(pos, state);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        updateConnections(world, state, pos);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        updateConnections(world, state, pos);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);

        for (Direction dir : Direction.values()) {
            CONNECTIONS.put(dir, BooleanProperty.of(dir.getName()));
            builder.add(CONNECTIONS.get(dir));
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        VoxelShape shape = CORE_SHAPE;

        for (Direction dir : Direction.values()) {
            if (hasConnection(state, dir)) {
                shape = switch (dir) {
                    case NORTH -> VoxelShapes.union(shape, Block.createCuboidShape(0, 0, 0, 16, 16, 1));
                    case SOUTH -> VoxelShapes.union(shape, Block.createCuboidShape(0, 0, 15, 16, 16, 16));
                    case EAST -> VoxelShapes.union(shape, Block.createCuboidShape(15, 0, 0, 16, 16, 16));
                    case WEST -> VoxelShapes.union(shape, Block.createCuboidShape(0, 0, 0, 1, 16, 16));
                    case UP -> VoxelShapes.union(shape, Block.createCuboidShape(0, 15, 0, 16, 16, 16));
                    case DOWN -> VoxelShapes.union(shape, Block.createCuboidShape(0, 0, 0, 16, 1, 16));
                };
            }
        }

        return shape;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.buildDefaultState(ctx);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
