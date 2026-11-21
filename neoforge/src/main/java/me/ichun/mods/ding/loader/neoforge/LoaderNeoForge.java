package me.ichun.mods.ding.loader.neoforge;

import me.ichun.mods.ding.common.Ding;
import me.ichun.mods.ding.common.client.DingConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Ding.MOD_ID, dist = Dist.CLIENT)
public class LoaderNeoForge {
    public LoaderNeoForge(ModContainer container) {
        Ding.init(FMLPaths.CONFIGDIR.get());
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (modContainer, parent) -> new DingConfigScreen(parent)
        );
    }
}
