package io.octalide.pipette.block.entity;

import io.octalide.pipette.pipe.FilterInventory;
import io.octalide.pipette.screen.FilterScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class PipeFilterEntity extends BlockEntity implements NamedScreenHandlerFactory, FilterInventory {
    public DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);
    
    public PipeFilterEntity(BlockPos pos, BlockState state) {
        super(Entities.PIPE_FILTER, pos, state);
    }

    public boolean filterIsEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    public boolean checkFilter(ItemStack toCheck) {
        if (filterIsEmpty()) {
            return true;
        }

        for (ItemStack stack : items) {
            if (stack.getItem() == toCheck.getItem()) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new FilterScreenHandler(syncId, inventory, this);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }
}
