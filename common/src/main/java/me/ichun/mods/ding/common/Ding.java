package me.ichun.mods.ding.common;

import com.mojang.logging.LogUtils;
import me.ichun.mods.ding.common.core.Config;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class Ding {
    public static final String MOD_ID = "ding";
    public static final String MOD_NAME = "Ding Continuation";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static Config config = new Config();

    private Ding() {
    }

    public static void init(Path configDir) {
        Path configPath = configDir.resolve(MOD_ID + ".toml");
        config = new Config(configPath);
        config.load();
        LOGGER.info("Initialized {} (playOnLoad={}, playOnWorld={}, playOnResourcesReload={})",
            MOD_NAME, config.playOnLoad, config.playOnWorld, config.playOnResourcesReload);
    }
}
