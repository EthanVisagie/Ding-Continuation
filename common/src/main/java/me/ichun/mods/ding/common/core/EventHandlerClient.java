package me.ichun.mods.ding.common.core;

import me.ichun.mods.ding.common.Ding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.Nullable;

public final class EventHandlerClient {
    private static boolean hasInit;
    private static boolean playWorld;

    private EventHandlerClient() {
    }

    public static boolean init() {
        if (!hasInit) {
            hasInit = true;
            if (Ding.config.playOnLoad) {
                playSound(Ding.config.name, (float) Ding.config.volume, (float) Ding.config.pitch, Ding.config.category);
            }
            return true;
        }
        return false;
    }

    public static void onOverlayChange(@Nullable Overlay currentOverlay, @Nullable Overlay newOverlay) {
        if (currentOverlay instanceof LoadingOverlay && newOverlay == null) {
            if (!init() && Ding.config.playOnResourcesReload) {
                playSound(
                    Ding.config.nameResourcesReload,
                    (float) Ding.config.volumeResourcesReload,
                    (float) Ding.config.pitchResourcesReload,
                    Ding.config.categoryResourcesReload
                );
            }
        }
    }

    public static void promptToPlayWorld() {
        if (Ding.config.playOnWorld) {
            playWorld = true;
        }
    }

    public static void onClientTickEnd() {
        if (!playWorld) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && (mc.player.tickCount > 20 || mc.isPaused())) {
            playWorld = false;
            if (Ding.config.playOnWorld) {
                playSound(
                    Ding.config.nameWorld,
                    (float) Ding.config.volumeWorld,
                    (float) Ding.config.pitchWorld,
                    Ding.config.categoryWorld
                );
            }
        }
    }

    public static void playSound(String name, float volume, float pitch, String categoryName) {
        Identifier id = Identifier.tryParse(name == null ? "" : name.trim());
        if (id == null) {
            Ding.LOGGER.warn("Invalid sound identifier configured in Ding: '{}'", name);
            return;
        }

        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.getOptional(id).orElse(null);
        SoundSource category = getCategoryByName(categoryName);

        // If the sound isn't in BuiltInRegistries, still attempt to play it by Identifier (supports resource-pack-only sounds)
        Minecraft.getInstance().getSoundManager().play(
            new SimpleSoundInstance(
                sound == null ? id : sound.location(),
                category,
                volume,
                pitch,
                SoundInstance.createUnseededRandom(),
                false,
                0,
                SoundInstance.Attenuation.NONE,
                0.0D,
                0.0D,
                0.0D,
                true
            )
        );

        if (sound == null) {
            Ding.LOGGER.warn("Could not find registered sound event but attempted to play anyway: {}", id);
        } else {
            Ding.LOGGER.info("Played ding sound '{}' (volume={}, pitch={}, category={})", id, volume, pitch, category.getName());
        }
    }

    public static SoundSource getCategoryByName(String name) {
        if (name != null) {
            for (SoundSource value : SoundSource.values()) {
                if (value.getName().equalsIgnoreCase(name.trim())) {
                    return value;
                }
            }
        }
        return SoundSource.MASTER;
    }
}
