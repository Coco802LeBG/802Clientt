package com.coco802.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class Module802 {
    public final String name;
    public final String description;
    private boolean enabled;

    protected Module802(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean value) {
        if (value == enabled) return;
        enabled = value;
        if (value) onEnable(); else onDisable();
    }

    public void toggle() { setEnabled(!enabled); }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick(MinecraftClient mc) {}
    public void onHud(DrawContext ctx, MinecraftClient mc) {}
}
