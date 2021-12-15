package io.octalide.pipette.client;

import io.octalide.pipette.screen.Screens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PipetteClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Screens.register();
    }
}
