package at.vibes.libNPC.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SpawnCustomizer {

    void handleSpawn(@NotNull NPC npc, @NotNull Player player);
}

