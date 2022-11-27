package at.vibes.libNPC.utils;

import at.vibes.libNPC.event.PlayerNPCHideEvent;
import at.vibes.libNPC.event.PlayerNPCInteractEvent;
import at.vibes.libNPC.modifier.AnimationModifier;
import at.vibes.libNPC.modifier.LabyModModifier;
import at.vibes.libNPC.modifier.MetadataModifier;
import at.vibes.libNPC.modifier.NPCModifier;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class NPCPool implements Listener {

    private static final Random RANDOM = new Random();

    protected final Plugin plugin;

    private final double spawnDistance;
    private final double actionDistance;
    private final long tabListRemoveTicks;

    private final Map<Integer, NPC> npcMap = new ConcurrentHashMap<>();

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public NPCPool(@NotNull Plugin plugin) {
        this(plugin, 50, 20, 30);
    }

    @Deprecated
    @ApiStatus.Internal
    public NPCPool(@NotNull Plugin plugin, int spawnDistance, int actionDistance,
                   long tabListRemoveTicks) {
        Preconditions.checkArgument(spawnDistance > 0 && actionDistance > 0, "Distance has to be > 0!");
        Preconditions.checkArgument(actionDistance <= spawnDistance,
                "Action distance cannot be higher than spawn distance!");

        this.plugin = plugin;

        //
        this.spawnDistance = Math.min(
                spawnDistance * spawnDistance,
                Math.pow(Bukkit.getViewDistance() << 4, 2));
        this.actionDistance = actionDistance * actionDistance;
        this.tabListRemoveTicks = tabListRemoveTicks;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        //
        String labyModPluginChannel = LabyModModifier.LABYMOD_PLUGIN_CHANNEL.getFullKey();
        //
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, labyModPluginChannel);
        if (!Bukkit.getMessenger().isIncomingChannelRegistered(plugin, labyModPluginChannel)) {
            Bukkit.getMessenger().registerIncomingPluginChannel(plugin, labyModPluginChannel,
                    (channel, player, message) -> {
                    });
        }

        this.addInteractListener();
        this.npcTick();
    }

    @NotNull
    public static NPCPool createDefault(@NotNull Plugin plugin) {
        return NPCPool.builder(plugin).build();
    }

    @NotNull
    public static Builder builder(@NotNull Plugin plugin) {
        return new Builder(plugin);
    }

    protected void addInteractListener() {
        ProtocolLibrary.getProtocolManager()
                .addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.USE_ENTITY) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PacketContainer container = event.getPacket();
                        int targetId = container.getIntegers().read(0);

                        if (NPCPool.this.npcMap.containsKey(targetId)) {
                            NPC npc = NPCPool.this.npcMap.get(targetId);

                            EnumWrappers.Hand usedHand;
                            EnumWrappers.EntityUseAction action;

                            if (NPCModifier.MINECRAFT_VERSION >= 17) {
                                WrappedEnumEntityUseAction useAction = container.getEnumEntityUseActions().read(0);

                                action = useAction.getAction();
                                usedHand = action == EnumWrappers.EntityUseAction.ATTACK
                                        ? EnumWrappers.Hand.MAIN_HAND
                                        : useAction.getHand();
                            } else {
                                action = container.getEntityUseActions().read(0);
                                usedHand = action == EnumWrappers.EntityUseAction.ATTACK
                                        ? EnumWrappers.Hand.MAIN_HAND
                                        : container.getHands().optionRead(0).orElse(EnumWrappers.Hand.MAIN_HAND);
                            }

                            Bukkit.getScheduler().runTask(
                                    NPCPool.this.plugin,
                                    () -> Bukkit.getPluginManager().callEvent(
                                            new PlayerNPCInteractEvent(
                                                    event.getPlayer(),
                                                    npc,
                                                    action,
                                                    usedHand))
                            );
                        }
                    }
                });
    }

    protected void npcTick() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            for (Player player : ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
                for (NPC npc : this.npcMap.values()) {
                    Location npcLoc = npc.getLocation();
                    Location playerLoc = player.getLocation();
                    if (!Objects.equals(npcLoc.getWorld(), playerLoc.getWorld())) {
                        if (npc.isShownFor(player)) {
                            npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.SPAWN_DISTANCE);
                        }
                        continue;
                    } else if (!Objects.requireNonNull(npcLoc.getWorld())
                            .isChunkLoaded(npcLoc.getBlockX() >> 4, npcLoc.getBlockZ() >> 4)) {
                        if (npc.isShownFor(player)) {
                            npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.UNLOADED_CHUNK);
                        }
                        continue;
                    }

                    double distance = npcLoc.distanceSquared(playerLoc);
                    boolean inRange = distance <= this.spawnDistance;

                    if ((npc.isExcluded(player) || !inRange) && npc.isShownFor(player)) {
                        npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.SPAWN_DISTANCE);
                    } else if ((!npc.isExcluded(player) && inRange) && !npc.isShownFor(player)) {
                        npc.show(player, this.plugin, this.tabListRemoveTicks);
                    }

                    if (npc.isShownFor(player) && npc.isLookAtPlayer() && distance <= this.actionDistance) {
                        npc.rotation().queueLookAt(playerLoc).send(player);
                    }
                }
            }
        }, 20, 2);
    }

    protected int getFreeEntityId() {
        int id;

        do {
            id = RANDOM.nextInt(Integer.MAX_VALUE);
        } while (this.npcMap.containsKey(id));

        return id;
    }

    protected void takeCareOf(@NotNull NPC npc) {
        this.npcMap.put(npc.getEntityId(), npc);
    }

    @Nullable
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public NPC getNPC(int entityId) {
        return this.npcMap.get(entityId);
    }

    @NotNull
    public Optional<NPC> getNpc(int entityId) {
        return Optional.ofNullable(this.npcMap.get(entityId));
    }

    @NotNull
    public Optional<NPC> getNpc(@NotNull UUID uniqueId) {
        return this.npcMap.values().stream()
                .filter(npc -> npc.getProfile().getUniqueId().equals(uniqueId)).findFirst();
    }

    public void removeNPC(int entityId) {
        this.getNpc(entityId).ifPresent(npc -> {
            this.npcMap.remove(entityId);
            npc.getSeeingPlayers()
                    .forEach(player -> npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.REMOVED));
        });
    }

    @NotNull
    @Unmodifiable
    public Collection<NPC> getNPCs() {
        return Collections.unmodifiableCollection(this.npcMap.values());
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        this.npcMap.values().stream()
                .filter(npc -> npc.isShownFor(player))
                .forEach(npc -> npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.RESPAWNED));
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.npcMap.values().stream()
                .filter(npc -> npc.isShownFor(player) || npc.isExcluded(player))
                .forEach(npc -> {
                    npc.removeSeeingPlayer(player);
                    npc.removeExcludedPlayer(player);
                });
    }

    @EventHandler
    public void handleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        for (NPC npc : this.npcMap.values()) {
            if (npc.isImitatePlayer()
                    && Objects.equals(npc.getLocation().getWorld(), player.getWorld())
                    && npc.isShownFor(player)
                    && npc.getLocation().distanceSquared(player.getLocation()) <= this.actionDistance) {
                npc.metadata()
                        .queue(MetadataModifier.EntityMetadata.SNEAKING, event.isSneaking()).send(player);
            }
        }
    }

    @EventHandler
    public void handleClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            for (NPC npc : this.npcMap.values()) {
                if (npc.isImitatePlayer()
                        && Objects.equals(npc.getLocation().getWorld(), player.getWorld())
                        && npc.isShownFor(player)
                        && npc.getLocation().distanceSquared(player.getLocation()) <= this.actionDistance) {
                    npc.animation().queue(AnimationModifier.EntityAnimation.SWING_MAIN_ARM)
                            .send(player);
                }
            }
        }
    }

    public static class Builder {
        private final Plugin plugin;

        private int spawnDistance = 50;
        private int actionDistance = 20;
        private long tabListRemoveTicks = 30;

        private Builder(@NotNull Plugin plugin) {
            this.plugin = Preconditions.checkNotNull(plugin, "plugin");
        }

        @NotNull
        public Builder spawnDistance(int spawnDistance) {
            Preconditions.checkArgument(spawnDistance > 0, "Spawn distance must be more than 0");
            this.spawnDistance = spawnDistance;
            return this;
        }

        @NotNull
        public Builder actionDistance(int actionDistance) {
            Preconditions.checkArgument(actionDistance > 0, "Action distance must be more than 0");
            this.actionDistance = actionDistance;
            return this;
        }

        @NotNull
        public Builder tabListRemoveTicks(long tabListRemoveTicks) {
            this.tabListRemoveTicks = tabListRemoveTicks;
            return this;
        }

        @NotNull
        public NPCPool build() {
            return new NPCPool(this.plugin, this.spawnDistance, this.actionDistance,
                    this.tabListRemoveTicks);
        }
    }
}