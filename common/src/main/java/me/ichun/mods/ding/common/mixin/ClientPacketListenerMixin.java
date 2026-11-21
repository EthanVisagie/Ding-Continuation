package me.ichun.mods.ding.common.mixin;

import me.ichun.mods.ding.common.core.EventHandlerClient;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void ding$onHandleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        EventHandlerClient.promptToPlayWorld();
    }
}
