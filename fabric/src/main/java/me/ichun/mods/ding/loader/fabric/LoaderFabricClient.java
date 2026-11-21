package me.ichun.mods.ding.loader.fabric;

import me.ichun.mods.ding.common.Ding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class LoaderFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Ding.init(FabricLoader.getInstance().getConfigDir());
    }
}
