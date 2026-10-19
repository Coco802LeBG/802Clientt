package com.coco802.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class HudModule extends Module802 {
    public HudModule() { super("HUD", "Affiche FPS et coordonnées"); }

    @Override
    public void onHud(DrawContext ctx, MinecraftClient mc) {
        if (mc.player == null) return;
        ctx.drawTextWithShadow(mc.textRenderer, "FPS: " + mc.getCurrentFps(), 4, 4, 0xFFFFFFFF);
        ctx.drawTextWithShadow(mc.textRenderer, String.format("XYZ: %.1f %.1f %.1f",
            mc.player.getX(), mc.player.getY(), mc.player.getZ()), 4, 16, 0xFFFFFFFF);
    }
}
