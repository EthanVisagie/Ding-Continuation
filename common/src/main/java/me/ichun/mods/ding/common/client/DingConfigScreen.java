package me.ichun.mods.ding.common.client;

import me.ichun.mods.ding.common.Ding;
import me.ichun.mods.ding.common.core.Config;
import me.ichun.mods.ding.common.core.EventHandlerClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

import java.util.Locale;

public class DingConfigScreen extends Screen {
    private enum Tab {
        LOAD("Game Load"),
        WORLD("World Load"),
        RESOURCES("Resource Reload");

        final String label;

        Tab(String label) {
            this.label = label;
        }
    }

    private final Screen parent;
    private Tab activeTab = Tab.LOAD;

    private boolean playOnLoad;
    private String nameLoad;
    private double volumeLoad;
    private double pitchLoad;
    private String categoryLoad;

    private boolean playOnWorld;
    private String nameWorld;
    private double volumeWorld;
    private double pitchWorld;
    private String categoryWorld;

    private boolean playOnResources;
    private String nameResources;
    private double volumeResources;
    private double pitchResources;
    private String categoryResources;

    private EditBox soundNameBox;

    public DingConfigScreen(Screen parent) {
        super(Component.literal(Ding.MOD_NAME + " Config"));
        this.parent = parent;
        loadFromConfig(Ding.config);
    }

    private void loadFromConfig(Config cfg) {
        playOnLoad = cfg.playOnLoad;
        nameLoad = cfg.name;
        volumeLoad = cfg.volume;
        pitchLoad = cfg.pitch;
        categoryLoad = cfg.category;

        playOnWorld = cfg.playOnWorld;
        nameWorld = cfg.nameWorld;
        volumeWorld = cfg.volumeWorld;
        pitchWorld = cfg.pitchWorld;
        categoryWorld = cfg.categoryWorld;

        playOnResources = cfg.playOnResourcesReload;
        nameResources = cfg.nameResourcesReload;
        volumeResources = cfg.volumeResourcesReload;
        pitchResources = cfg.pitchResourcesReload;
        categoryResources = cfg.categoryResourcesReload;
    }

    private void saveToConfig() {
        flushActiveEditBox();
        Config cfg = Ding.config;
        cfg.playOnLoad = playOnLoad;
        cfg.name = nameLoad;
        cfg.volume = volumeLoad;
        cfg.pitch = pitchLoad;
        cfg.category = categoryLoad;

        cfg.playOnWorld = playOnWorld;
        cfg.nameWorld = nameWorld;
        cfg.volumeWorld = volumeWorld;
        cfg.pitchWorld = pitchWorld;
        cfg.categoryWorld = categoryWorld;

        cfg.playOnResourcesReload = playOnResources;
        cfg.nameResourcesReload = nameResources;
        cfg.volumeResourcesReload = volumeResources;
        cfg.pitchResourcesReload = pitchResources;
        cfg.categoryResourcesReload = categoryResources;
        cfg.save();
    }

    private void flushActiveEditBox() {
        if (soundNameBox == null) {
            return;
        }
        String val = soundNameBox.getValue().trim();
        switch (activeTab) {
            case LOAD -> nameLoad = val;
            case WORLD -> nameWorld = val;
            case RESOURCES -> nameResources = val;
        }
    }

    private boolean getEnabled() {
        return switch (activeTab) {
            case LOAD -> playOnLoad;
            case WORLD -> playOnWorld;
            case RESOURCES -> playOnResources;
        };
    }

    private void setEnabled(boolean value) {
        switch (activeTab) {
            case LOAD -> playOnLoad = value;
            case WORLD -> playOnWorld = value;
            case RESOURCES -> playOnResources = value;
        }
    }

    private String getSoundName() {
        return switch (activeTab) {
            case LOAD -> nameLoad;
            case WORLD -> nameWorld;
            case RESOURCES -> nameResources;
        };
    }

    private double getVolume() {
        return switch (activeTab) {
            case LOAD -> volumeLoad;
            case WORLD -> volumeWorld;
            case RESOURCES -> volumeResources;
        };
    }

    private void setVolume(double value) {
        switch (activeTab) {
            case LOAD -> volumeLoad = value;
            case WORLD -> volumeWorld = value;
            case RESOURCES -> volumeResources = value;
        }
    }

    private double getPitch() {
        return switch (activeTab) {
            case LOAD -> pitchLoad;
            case WORLD -> pitchWorld;
            case RESOURCES -> pitchResources;
        };
    }

    private void setPitch(double value) {
        switch (activeTab) {
            case LOAD -> pitchLoad = value;
            case WORLD -> pitchWorld = value;
            case RESOURCES -> pitchResources = value;
        }
    }

    private String getCategory() {
        return switch (activeTab) {
            case LOAD -> categoryLoad;
            case WORLD -> categoryWorld;
            case RESOURCES -> categoryResources;
        };
    }

    private void setCategory(String value) {
        switch (activeTab) {
            case LOAD -> categoryLoad = value;
            case WORLD -> categoryWorld = value;
            case RESOURCES -> categoryResources = value;
        }
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int tabWidth = 104;
        int tabGap = 6;
        int tabsStartX = centerX - (tabWidth * 3 + tabGap * 2) / 2;
        int topY = 36;

        Tab[] tabs = Tab.values();
        for (int i = 0; i < tabs.length; i++) {
            Tab tab = tabs[i];
            Button tabBtn = Button.builder(Component.literal(tab.label), btn -> {
                flushActiveEditBox();
                activeTab = tab;
                this.rebuildWidgets();
            }).bounds(tabsStartX + i * (tabWidth + tabGap), topY, tabWidth, 20).build();
            tabBtn.active = (tab != activeTab);
            this.addRenderableWidget(tabBtn);
        }

        int fieldWidth = 260;
        int leftX = centerX - fieldWidth / 2;
        int y = 70;

        this.addRenderableWidget(Button.builder(
            Component.literal("Enabled: " + (getEnabled() ? "ON" : "OFF")),
            btn -> {
                boolean next = !getEnabled();
                setEnabled(next);
                btn.setMessage(Component.literal("Enabled: " + (next ? "ON" : "OFF")));
            }
        ).bounds(leftX, y, fieldWidth, 20).build());

        y += 38;
        this.soundNameBox = new EditBox(this.font, leftX, y, fieldWidth, 20, Component.literal("Sound ID"));
        this.soundNameBox.setMaxLength(256);
        this.soundNameBox.setValue(getSoundName());
        this.addRenderableWidget(this.soundNameBox);

        y += 28;
        this.addRenderableWidget(new DoubleSlider(leftX, y, fieldWidth, 20, "Volume", 0.0D, 2.0D, getVolume(), this::setVolume));

        y += 26;
        this.addRenderableWidget(new DoubleSlider(leftX, y, fieldWidth, 20, "Pitch", 0.0D, 2.0D, getPitch(), this::setPitch));

        y += 26;
        this.addRenderableWidget(Button.builder(
            Component.literal("Category: " + EventHandlerClient.getCategoryByName(getCategory()).getName()),
            btn -> {
                SoundSource[] sources = SoundSource.values();
                SoundSource current = EventHandlerClient.getCategoryByName(getCategory());
                SoundSource next = sources[(current.ordinal() + 1) % sources.length];
                setCategory(next.getName());
                btn.setMessage(Component.literal("Category: " + next.getName()));
            }
        ).bounds(leftX, y, fieldWidth, 20).build());

        y += 28;
        this.addRenderableWidget(Button.builder(
            Component.literal("Test Sound"),
            btn -> {
                flushActiveEditBox();
                EventHandlerClient.playSound(getSoundName(), (float) getVolume(), (float) getPitch(), getCategory());
            }
        ).bounds(leftX, y, fieldWidth, 20).build());

        int footerY = this.height - 28;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, btn -> onClose())
            .bounds(centerX - 104, footerY, 100, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> {
            saveToConfig();
            onClose();
        }).bounds(centerX + 4, footerY, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 14, 0xFFFFFFFF);
        int leftX = this.width / 2 - 130;
        graphics.drawString(this.font, Component.literal("Sound ID (e.g. entity.experience_orb.pickup):"), leftX, 96, 0xFFA0A0A0);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    private static final class DoubleSlider extends AbstractSliderButton {
        private final String prefix;
        private final double min;
        private final double max;
        private final java.util.function.DoubleConsumer setter;

        DoubleSlider(int x, int y, int width, int height, String prefix, double min, double max, double initial, java.util.function.DoubleConsumer setter) {
            super(x, y, width, height, Component.empty(), clampUnit((initial - min) / (max - min)));
            this.prefix = prefix;
            this.min = min;
            this.max = max;
            this.setter = setter;
            updateMessage();
        }

        private static double clampUnit(double v) {
            return Math.max(0.0D, Math.min(1.0D, v));
        }

        private double currentValue() {
            double raw = min + this.value * (max - min);
            return Math.round(raw * 20.0D) / 20.0D;
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.literal(String.format(Locale.ROOT, "%s: %.2f", prefix, currentValue())));
        }

        @Override
        protected void applyValue() {
            setter.accept(currentValue());
        }
    }
}
