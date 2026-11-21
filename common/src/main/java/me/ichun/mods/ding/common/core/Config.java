package me.ichun.mods.ding.common.core;

import me.ichun.mods.ding.common.Ding;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class Config {
    public boolean playOnLoad = true;
    public String name = "entity.experience_orb.pickup";
    public double volume = 0.25D;
    public double pitch = 1.0D;
    public String category = "master";

    public boolean playOnWorld = false;
    public String nameWorld = "entity.experience_orb.pickup";
    public double volumeWorld = 0.25D;
    public double pitchWorld = 1.0D;
    public String categoryWorld = "master";

    public boolean playOnResourcesReload = true;
    public String nameResourcesReload = "entity.experience_orb.pickup";
    public double volumeResourcesReload = 0.25D;
    public double pitchResourcesReload = 1.0D;
    public String categoryResourcesReload = "master";

    private final Path configPath;

    public Config() {
        this(null);
    }

    public Config(Path configPath) {
        this.configPath = configPath;
    }

    public void load() {
        if (configPath == null) {
            return;
        }
        if (!Files.exists(configPath)) {
            save();
            return;
        }

        try {
            List<String> lines = Files.readAllLines(configPath, StandardCharsets.UTF_8);
            String section = "";
            for (String rawLine : lines) {
                String line = stripComment(rawLine).trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("[") && line.endsWith("]")) {
                    section = line.substring(1, line.length() - 1).trim().toLowerCase(Locale.ROOT);
                    continue;
                }
                int eq = line.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = line.substring(0, eq).trim();
                String val = line.substring(eq + 1).trim();
                applyKey(section, key, val);
            }
        } catch (IOException e) {
            Ding.LOGGER.error("Failed to read config file {}", configPath, e);
        }

        clampValues();
        save();
    }

    public void save() {
        if (configPath == null) {
            return;
        }
        clampValues();
        String content = String.format(
            Locale.ROOT,
            """
            # Ding Continuation Configuration (compatible with original Ding config format)

            # Sound played when Minecraft finishes loading to the main menu
            [load]
            playOnLoad = %s
            name = "%s"
            # Range: 0.0 ~ 10.0
            volume = %s
            # Range: 0.0 ~ 10.0
            pitch = %s
            # Sound category (master, music, record, weather, block, hostile, neutral, player, ambient, voice)
            category = "%s"

            # Sound played when a world finishes loading after connecting to a server/world
            [world]
            playOnWorld = %s
            nameWorld = "%s"
            # Range: 0.0 ~ 10.0
            volumeWorld = %s
            # Range: 0.0 ~ 10.0
            pitchWorld = %s
            categoryWorld = "%s"

            # Sound played when resource packs finish reloading
            [resourcesReload]
            playOnResourcesReload = %s
            nameResourcesReload = "%s"
            # Range: 0.0 ~ 10.0
            volumeResourcesReload = %s
            # Range: 0.0 ~ 10.0
            pitchResourcesReload = %s
            categoryResourcesReload = "%s"
            """,
            playOnLoad, escapeToml(name), formatDouble(volume), formatDouble(pitch), escapeToml(category),
            playOnWorld, escapeToml(nameWorld), formatDouble(volumeWorld), formatDouble(pitchWorld), escapeToml(categoryWorld),
            playOnResourcesReload, escapeToml(nameResourcesReload), formatDouble(volumeResourcesReload), formatDouble(pitchResourcesReload), escapeToml(categoryResourcesReload)
        );

        try {
            Path parent = configPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(configPath, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Ding.LOGGER.error("Failed to write config file {}", configPath, e);
        }
    }

    private void applyKey(String section, String key, String rawVal) {
        switch (key) {
            case "playOnLoad" -> playOnLoad = parseBoolean(rawVal, playOnLoad);
            case "name" -> {
                if (section.isEmpty() || section.equals("load")) {
                    name = unquote(rawVal);
                }
            }
            case "volume" -> {
                if (section.isEmpty() || section.equals("load")) {
                    volume = parseDouble(rawVal, volume);
                }
            }
            case "pitch" -> {
                if (section.isEmpty() || section.equals("load")) {
                    pitch = parseDouble(rawVal, pitch);
                }
            }
            case "category" -> {
                if (section.isEmpty() || section.equals("load")) {
                    category = unquote(rawVal);
                }
            }
            case "playOnWorld" -> playOnWorld = parseBoolean(rawVal, playOnWorld);
            case "nameWorld" -> nameWorld = unquote(rawVal);
            case "volumeWorld" -> volumeWorld = parseDouble(rawVal, volumeWorld);
            case "pitchWorld" -> pitchWorld = parseDouble(rawVal, pitchWorld);
            case "categoryWorld" -> categoryWorld = unquote(rawVal);
            case "playOnResourcesReload" -> playOnResourcesReload = parseBoolean(rawVal, playOnResourcesReload);
            case "nameResourcesReload" -> nameResourcesReload = unquote(rawVal);
            case "volumeResourcesReload" -> volumeResourcesReload = parseDouble(rawVal, volumeResourcesReload);
            case "pitchResourcesReload" -> pitchResourcesReload = parseDouble(rawVal, pitchResourcesReload);
            case "categoryResourcesReload" -> categoryResourcesReload = unquote(rawVal);
            default -> {}
        }
    }

    private void clampValues() {
        volume = clamp(volume, 0.0D, 10.0D);
        pitch = clamp(pitch, 0.0D, 10.0D);
        volumeWorld = clamp(volumeWorld, 0.0D, 10.0D);
        pitchWorld = clamp(pitchWorld, 0.0D, 10.0D);
        volumeResourcesReload = clamp(volumeResourcesReload, 0.0D, 10.0D);
        pitchResourcesReload = clamp(pitchResourcesReload, 0.0D, 10.0D);
    }

    private static double clamp(double value, double min, double max) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }

    private static boolean parseBoolean(String value, boolean fallback) {
        String clean = unquote(value).toLowerCase(Locale.ROOT);
        if ("true".equals(clean)) {
            return true;
        }
        if ("false".equals(clean)) {
            return false;
        }
        return fallback;
    }

    private static double parseDouble(String value, double fallback) {
        try {
            return Double.parseDouble(unquote(value));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static String unquote(String value) {
        String trimmed = value.trim();
        if (trimmed.length() >= 2 && ((trimmed.startsWith("\"") && trimmed.endsWith("\"")) || (trimmed.startsWith("'") && trimmed.endsWith("'")))) {
            return trimmed.substring(1, trimmed.length() - 1)
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
        }
        return trimmed;
    }

    private static String escapeToml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String formatDouble(double value) {
        double rounded = Math.round(value * 1000.0D) / 1000.0D;
        return Double.toString(rounded);
    }

    private static String stripComment(String line) {
        boolean inDouble = false;
        boolean inSingle = false;
        boolean escaped = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escaped) {
                escaped = false;
                continue;
            }
            if (c == '\\' && inDouble) {
                escaped = true;
                continue;
            }
            if (c == '"' && !inSingle) {
                inDouble = !inDouble;
            } else if (c == '\'' && !inDouble) {
                inSingle = !inSingle;
            } else if (c == '#' && !inDouble && !inSingle) {
                return line.substring(0, i);
            }
        }
        return line;
    }
}
