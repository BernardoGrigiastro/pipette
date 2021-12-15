package io.octalide.pipette.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class Msg {
    public static void send(String message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.inGameHud.getChatHud().addMessage(new LiteralText(message));
    }
}
