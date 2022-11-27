package at.vibes.libHolograms;

import at.vibes.libHolograms.other.Hologram;
import at.vibes.libHolograms.other.HologramPool;
import at.vibes.libHolograms.placeholder.Placeholders;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LibHolograms {
    private static LibHolograms instance;

    LibHolograms() {instance = this;}

    public abstract Hologram getHologram();
    public abstract Hologram getHologram(@NotNull Plugin plugin, @NotNull Location location, @Nullable Placeholders placeholders, @NotNull Object[]... l);

    public abstract Hologram.Builder getHologramBuilder();

    public abstract HologramPool getHologramPool();
    public abstract HologramPool getHologramPool(@NotNull Plugin plugin, double spawnDistance, float minHitDistance, float maxHitDistance);

    public static LibHolograms getInstance()  {return instance;}
}
