package io.octalide.pipette.block.entity;

import java.util.ArrayList;

import io.octalide.pipette.block.IPipeBlock;
import io.octalide.pipette.block.PipeFilterBlock;
import io.octalide.pipette.block.PipePullerBlock;
import io.octalide.pipette.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PipePullerEntity extends BlockEntity {
    public PipePullerEntity(BlockPos pos, BlockState state) {
        super(Entities.PIPE_PULLER, pos, state);
    }

    public ArrayList<BlockPos> findFilters(World world, BlockPos pos) {
        ArrayList<BlockPos> found = new ArrayList<BlockPos>();

        ArrayList<BlockPos> todo = new ArrayList<>();
        ArrayList<BlockPos> done = new ArrayList<>();

        todo.add(pos);

        while(!todo.isEmpty()) {
            BlockPos cpos = todo.remove(0);
            done.add(cpos);

            BlockState state = world.getBlockState(cpos);
            Block block = state.getBlock();

            if (block instanceof PipeFilterBlock) {
                if (!found.contains(cpos)) {
                    found.add(cpos);
                }
            }

            if (block instanceof IPipeBlock pipe) {
                for (Direction dir : pipe.connections(state)) {
                    BlockPos npos = cpos.offset(dir);

                    if (!done.contains(npos) && !todo.contains(npos)) {
                        todo.add(npos);
                    }
                }
            }
        }

        return found;
    }

    public static void tick(World world, BlockPos pos, BlockState state, PipePullerEntity ppe) {
        if (world.isClient()) {
            return;
        }

        PipePullerBlock puller = (PipePullerBlock) state.getBlock();
        Inventory from = puller.getInput(world, state, pos);
        if (from == null) {
            return;
        }

        if (from.isEmpty()) {
            return;
        }

        Direction pside = puller.facing(state);

        ArrayList<BlockPos> filters = ppe.findFilters(world, pos);

        for (BlockPos fpos : filters) {
            BlockState fstate = world.getBlockState(fpos);
            PipeFilterBlock fblock = (PipeFilterBlock) fstate.getBlock();
            PipeFilterEntity fentity = (PipeFilterEntity) world.getBlockEntity(fpos);
            if (fentity == null) {
                continue;
            }

            Direction fside = fblock.facing(fstate).getOpposite();
            
            Inventory to = fblock.getOutput(world, fstate, fpos);
            if (to == null) {
                continue;
            }
    
            if(Util.move(from, to, pside, fside, fentity.getItems())) {
                return;
            }
        }
    }
}
