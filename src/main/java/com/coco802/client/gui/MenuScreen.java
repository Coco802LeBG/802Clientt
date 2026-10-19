package com.coco802.client.gui;

import com.coco802.client.Client802;
import com.coco802.client.module.Module802;
import com.deathmotion.marlowcrystal.MarlowCrystal;
import com.deathmotion.marlowcrystal.config.Settings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class MenuScreen extends Screen {
    private static final int[] SCALES = {0, 1, 2, 3, 4, 5, 6};
    private static final int[] RELEASE_PRESETS = {500, 1000, 1500, 2000, 3000};
    private static int page = 0;
    private final Screen parent;

    public MenuScreen(Screen parent) {
        super(Text.literal("802 Client"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        String[] tabs = {"Modules", "GUI Scale", "Crystal Reflex"};
        for (int i = 0; i < tabs.length; i++) {
            final int idx = i;
            Text label = Text.literal((page == i ? "> " : "") + tabs[i]);
            this.addDrawableChild(ButtonWidget.builder(label, b -> { page = idx; this.clearAndInit(); })
                .dimensions(cx - 155 + i * 105, 32, 100, 20).build());
        }
        int y = 66;
        if (page == 0) {
            for (Module802 m : Client802.MODULES) {
                this.addDrawableChild(ButtonWidget.builder(Text.literal(m.name + " : " + (m.isEnabled() ? "ON" : "OFF")), b -> {
                    m.toggle();
                    Client802.saveModules();
                    this.clearAndInit();
                }).dimensions(cx - 100, y, 200, 20).build());
                y += 24;
            }
        } else if (page == 1) {
            for (int i = 0; i < GuiScaleManager.KINDS.length; i++) {
                final String kind = GuiScaleManager.KINDS[i];
                int current = Client802.CONFIG.scaleFor(kind);
                String value = current == 0 ? "Auto" : String.valueOf(current);
                this.addDrawableChild(ButtonWidget.builder(Text.literal(GuiScaleManager.LABELS[i] + " : " + value), b -> {
                    int cur = Client802.CONFIG.scaleFor(kind);
                    int idx = 0;
                    for (int k = 0; k < SCALES.length; k++) if (SCALES[k] == cur) idx = k;
                    Client802.CONFIG.guiScale.put(kind, SCALES[(idx + 1) % SCALES.length]);
                    Client802.CONFIG.save();
                    this.clearAndInit();
                }).dimensions(cx - 100, y, 200, 20).build());
                y += 24;
            }
        } else {
            Settings s = MarlowCrystal.get().settings();
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Keep Render : " + (s.isKeepRender() ? "ON" : "OFF")), b -> {
                s.setKeepRender(!s.isKeepRender());
                MarlowCrystal.get().saveSettings();
                this.clearAndInit();
            }).dimensions(cx - 100, y, 200, 20).build());
            y += 24;
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Raw mouse input : " + (s.isRawMouseInput() ? "ON" : "OFF")), b -> {
                s.setRawMouseInput(!s.isRawMouseInput());
                MarlowCrystal.get().saveSettings();
                this.clearAndInit();
            }).dimensions(cx - 100, y, 200, 20).build());
            y += 24;
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Délai de libération : " + s.getKeepReleaseMs() + " ms"), b -> {
                int cur = s.getKeepReleaseMs();
                int next = RELEASE_PRESETS[0];
                for (int i = 0; i < RELEASE_PRESETS.length; i++) {
                    if (RELEASE_PRESETS[i] > cur) { next = RELEASE_PRESETS[i]; break; }
                }
                s.setKeepReleaseMs(next);
                MarlowCrystal.get().saveSettings();
                this.clearAndInit();
            }).dimensions(cx - 100, y, 200, 20).build());
            y += 24;
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Crystals gardés max : " + s.getMaxKeptCrystals()), b -> {
                int next = s.getMaxKeptCrystals() + 1;
                if (next > 8) next = 1;
                s.setMaxKeptCrystals(next);
                MarlowCrystal.get().saveSettings();
                this.clearAndInit();
            }).dimensions(cx - 100, y, 200, 20).build());
        }
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Fermer"), b -> this.close())
            .dimensions(cx - 50, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 14, 0xFF55AAFF);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
