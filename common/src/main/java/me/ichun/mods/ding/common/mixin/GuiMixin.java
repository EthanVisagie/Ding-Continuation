package me.ichun.mods.ding.common.mixin;

import me.ichun.mods.ding.common.core.EventHandlerClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Overlay;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    @Nullable
    private Overlay overlay;

    @Inject(method = "setOverlay", at = @At("HEAD"))
    private void ding$onSetOverlay(@Nullable Overlay newOverlay, CallbackInfo ci) {
        EventHandlerClient.onOverlayChange(this.overlay, newOverlay);
    }
}
