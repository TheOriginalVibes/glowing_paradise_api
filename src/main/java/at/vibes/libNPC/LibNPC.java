package at.vibes.libNPC;

import at.vibes.libNPC.profile.Profile;
import at.vibes.libNPC.utils.NPC;
import at.vibes.libNPC.utils.NPCPool;
import at.vibes.libNPC.utils.SpawnCustomizer;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LibNPC {
    private static LibNPC instance;

    LibNPC() {instance = this;}

    public abstract NPC getNPC();
    public abstract NPC getNPC(@NotNull Plugin plugin, @Nullable Profile profile, @NotNull Location location, @NotNull SpawnCustomizer spawnCustomizer, int entityId, boolean lookAtPlayer, boolean imitatePlayer, boolean usePlayerProfiles, @NotNull Object... lines);
    public abstract NPC.Builder getNPCBuilder();

    public abstract NPCPool getNPCPool();
    public abstract NPCPool getNPCPool(@NotNull Plugin plugin);
    public abstract NPCPool getNPCPool(@NotNull Plugin plugin, int spawnDistance, int actionDistance, long tabListRemoveTicks);
    public abstract NPCPool.Builder getNPCPoolBuilder();

    public abstract SpawnCustomizer getSpawnCustomizer();

    public static LibNPC getInstance() {return instance;}
}
