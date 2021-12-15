package io.octalide.pipette.screen;

import io.octalide.pipette.Pipette;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Screens {
    public static final ScreenHandlerType<FilterScreenHandler> FILTER_SCREEN_HANDLER;

    static {
        FILTER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Pipette.MOD_ID, "filter"), FilterScreenHandler::new);
    }

    public static void register() {
        ScreenRegistry.register(FILTER_SCREEN_HANDLER, FilterScreen::new);
    }
}
