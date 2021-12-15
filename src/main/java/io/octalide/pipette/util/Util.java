package io.octalide.pipette.util;

import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Util {
    public static Inventory getInventoryAt(World world, BlockPos pos) {
        Inventory inventory = null;

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider) block).getInventory(state, world, pos);
        } else if (state.hasBlockEntity()) {
            BlockEntity be = world.getBlockEntity(pos);

            if (be instanceof Inventory) {
                inventory = (Inventory) be;

                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getInventory((ChestBlock) block, state, world, pos, true);
                }
            }
        }

        return inventory;
    }

    public static boolean move(Inventory from, Inventory to, Direction fromSide, Direction toSide, DefaultedList<ItemStack> whitelist) {
        if (isInventoryFull(to, toSide)) {
            return false;
        }

        return getAvailableSlots(from, fromSide).anyMatch(slot -> {
            ItemStack stackCopy = from.getStack(slot).copy();
            if (stackCopy.isEmpty() || !canExtract(from, stackCopy, slot, fromSide)) {
                return false;
            }

            if (whitelist != null && whitelist.size() > 0) {
                boolean pass = false;
                boolean empty = true;
                for (ItemStack stack : whitelist) {
                    if (stack.isEmpty()) {
                        continue;
                    }

                    empty = false;
    
                    if (stackCopy.getItem() == stack.getItem()) {
                        pass = true;
                        break;
                    }
                }

                if (!pass && !empty) {
                    return false;
                }
            }

            ItemStack stackDiff = HopperBlockEntity.transfer(from, to, from.removeStack(slot, 1), toSide);
            if (stackDiff.isEmpty()) {
                to.markDirty();
                return true;
            }

            from.setStack(slot, stackCopy);
            return false;
        });
    }

    public static boolean transfer(Inventory from, Inventory to, Direction side) {
        if (isInventoryFull(to, side)) {
            return false;
        }

        return getAvailableSlots(from, side).anyMatch(slot -> {
            ItemStack stackCopy = from.getStack(slot).copy();
            if (stackCopy.isEmpty() || !canExtract(from, stackCopy, slot, side)) {
                return false;
            }

            ItemStack stackDiff = HopperBlockEntity.transfer(from, to, from.removeStack(slot, 1), side);
            if (stackDiff.isEmpty()) {
                to.markDirty();
                return true;
            }

            from.setStack(slot, stackCopy);
            return false;
        });
    }

    public static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
        return !(inv instanceof SidedInventory) || ((SidedInventory)inv).canExtract(slot, stack, facing);
     }

    public static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        return inventory instanceof SidedInventory
                ? IntStream.of(((SidedInventory) inventory).getAvailableSlots(side))
                : IntStream.range(0, inventory.size());
    }

    public static boolean isInventoryFull(Inventory inventory, Direction direction) {
        return getAvailableSlots(inventory, direction).allMatch(i -> (inventory.getStack(i).getMaxCount() - inventory.getStack(i).getCount()) <= 0);
    }
}
