package io.octalide.pipette.util;

import net.minecraft.util.math.BlockPos;

public class Nbt {
    public static BlockPos toPos(int[] pos) {
        return new BlockPos(pos[0], pos[1], pos[2]);
    }

    public static int[] toIntArray(BlockPos master) {
        return new int[]{master.getX(), master.getY(), master.getZ()};
    }
}

