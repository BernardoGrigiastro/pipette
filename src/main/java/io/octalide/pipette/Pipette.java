package io.octalide.pipette;

import io.octalide.pipette.block.Blocks;
import io.octalide.pipette.block.entity.Entities;
import io.octalide.pipette.item.Items;
import net.fabricmc.api.ModInitializer;

public class Pipette implements ModInitializer {
    public static final String MOD_ID = "pipette";

    @Override
    public void onInitialize() {
        Blocks.register();
        Items.register();
        Entities.register();
    }
}
