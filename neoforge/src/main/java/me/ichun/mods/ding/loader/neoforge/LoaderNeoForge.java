package me.ichun.mods.ding.loader.neoforge;

import me.ichun.mods.ding.common.Ding;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(value = Ding.MOD_ID, dist = Dist.CLIENT)
public class LoaderNeoForge {
    public LoaderNeoForge(ModContainer container) {
        Ding.init(FMLPaths.CONFIGDIR.get());
    }
}
