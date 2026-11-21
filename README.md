# Ding Continuation

Ding Continuation is an unofficial continuation of [Ding](https://github.com/iChun/Ding) by **iChun**, updated for modern Minecraft versions (starting with Minecraft 1.21.11 on Fabric and NeoForge).

All credit for the original mod concept, design, and codebase goes to **iChun**. This fork exists solely to keep Ding working on newer versions of Minecraft while staying true to the original mod.

## What Does It Do?

When you launch a large modpack, Minecraft can take a while to load. Ding Continuation plays a short sound effect so you know the game is ready even if you are tabbed out doing something else.

It can play a configurable sound for three events:

1. **Game Load:** Plays when Minecraft finishes starting up and reaches the Main Menu. Enabled by default.
2. **Resource Reload:** Plays when resource packs finish reloading (such as pressing F3 + T or switching packs). Enabled by default.
3. **World Load:** Plays after you finish loading into a world or server. Disabled by default.

## Configuring Sounds

You can configure Ding Continuation in two ways:

1. **Inside the game:** Open **Mods > Ding Continuation > Config** on NeoForge (or through **Mod Menu** on Fabric). You can switch between Game Load, World Load, and Resource Reload, change the sound ID, adjust volume, pitch, and sound category, and click **Test Sound** to preview your sound right away.
2. **By editing `config/ding.toml`:** Edit the generated file in your Minecraft `config` folder with any text editor.

## Supported Versions

1. Minecraft 1.21.11 (Fabric and NeoForge)

## License & Credits

Original mod created by **iChun**: https://github.com/iChun/Ding

Licensed under the **GNU Lesser General Public License v3.0** (`COPYING` and `COPYING.LESSER`), in accordance with the original repository.
