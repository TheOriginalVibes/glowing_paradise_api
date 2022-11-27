package at.vibes.libHolograms.other;

import at.vibes.libHolograms.event.PlayerHologramInteractEvent;
import at.vibes.libHolograms.util.AABB;
import at.vibes.libHolograms.util.Validate;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;


public class HologramPool implements Listener {

    protected static AtomicInteger IDs_COUNTER = new AtomicInteger(new Random().nextInt());

    private final Plugin plugin;
    private final double spawnDistance;
    private final float minHitDistance;
    private final float maxHitDistance;

    private final Collection<Hologram> holograms = new CopyOnWriteArraySet<>();

    public HologramPool(@NotNull Plugin plugin, double spawnDistance, float minHitDistance,
                        float maxHitDistance) {
        Validate.notNull(plugin, "Plugin cannot be null");
        if (minHitDistance < 0) {
            throw new IllegalArgumentException("minHitDistance must be positive");
        }
        if (maxHitDistance > 120) {
            throw new IllegalArgumentException("maxHitDistance cannot be greater than 120");
        }
        this.plugin = plugin;
        this.spawnDistance = spawnDistance * spawnDistance;
        this.minHitDistance = minHitDistance;
        this.maxHitDistance = maxHitDistance;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        hologramTick();
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        holograms.stream()
                .filter(h -> h.isShownFor(player))
                .forEach(h -> h.hide(player));
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        holograms.stream()
                .filter(h -> h.isShownFor(player) || h.isExcluded(player))
                .forEach(h -> {
                    h.removeSeeingPlayer(player);
                    h.removeExcludedPlayer(player);
                });
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        if (e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }
        FST:
        for (Hologram hologram : holograms) {
            if (!hologram.isShownFor(player)) {
                continue;
            }
            for (AbstractLine<?> line : hologram.lines) {
                if (!(line instanceof TextLine)) {
                    continue;
                }

                TextLine tL = (TextLine) line;
                if (!tL.isClickable() || tL.hitbox == null) {
                    continue;
                }

                AABB.Vec3D intersects = tL.hitbox.intersectsRay(
                        new AABB.Ray3D(player.getEyeLocation()), minHitDistance, maxHitDistance);
                if (intersects == null) {
                    continue;
                }

                Bukkit.getScheduler().runTask(
                        plugin,
                        () -> Bukkit.getPluginManager()
                                .callEvent(new PlayerHologramInteractEvent(player, hologram, tL)));
                break FST;
            }
        }
    }

    protected Plugin getPlugin() {
        return plugin;
    }

    protected void takeCareOf(@NotNull Hologram hologram) {
        this.holograms.add(hologram);
    }

    protected void hologramTick() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            for (Player player : ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
                for (Hologram hologram : this.holograms) {
                    Location holoLoc = hologram.getLocation();
                    Location playerLoc = player.getLocation();
                    boolean isShown = hologram.isShownFor(player);

                    if (!Objects.equals(holoLoc.getWorld(), playerLoc.getWorld())) {
                        if (isShown) {
                            hologram.hide(player);
                        }
                        continue;
                    } else if (!holoLoc.getWorld()
                            .isChunkLoaded(holoLoc.getBlockX() >> 4, holoLoc.getBlockZ() >> 4) && isShown) {
                        hologram.hide(player);
                        continue;
                    }
                    boolean inRange = holoLoc.distanceSquared(playerLoc) <= this.spawnDistance;

                    if ((hologram.isExcluded(player) || !inRange) && isShown) {
                        hologram.hide(player);
                    } else if (!hologram.isExcluded(player) && inRange && !isShown) {
                        hologram.show(player);
                    }
                }
            }
        }, 20L, 2L);
    }

    public void remove(@NotNull Hologram hologram) {
        Validate.notNull(hologram, "Hologram to remove cannot be null");
        if (this.holograms.contains(hologram)) {
            this.holograms.remove(hologram);
            hologram.getSeeingPlayers()
                    .forEach(hologram::hide);
        }
    }
}