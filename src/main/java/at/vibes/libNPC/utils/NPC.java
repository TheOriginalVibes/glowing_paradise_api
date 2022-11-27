package at.vibes.libNPC.utils;

import at.vibes.libNPC.event.PlayerNPCHideEvent;
import at.vibes.libNPC.event.PlayerNPCShowEvent;
import at.vibes.libNPC.modifier.*;
import at.vibes.libNPC.profile.Profile;
import at.vibes.libNPC.profile.ProfileUtils;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class NPC {

    private final Set<Player> seeingPlayers = new CopyOnWriteArraySet<>();
    private final Collection<Player> excludedPlayers = new CopyOnWriteArraySet<>();

    private final int entityId;
    private final boolean usePlayerProfiles;

    private final Profile profile;

    private Location location;

    private final WrappedGameProfile gameProfile;
    private final SpawnCustomizer spawnCustomizer;

    private boolean lookAtPlayer;
    private boolean imitatePlayer;

    private NPC(
            @NotNull Plugin plugin,
            @Nullable Profile profile,
            @NotNull Location location,
            @NotNull SpawnCustomizer spawnCustomizer,
            int entityId,
            boolean lookAtPlayer,
            boolean imitatePlayer,
            boolean usePlayerProfiles,
            //@NotNull Placeholders placeholders,
            @NotNull Object... lines
    ) {
        this.entityId = entityId;

        this.location = location;
        this.spawnCustomizer = spawnCustomizer;

        this.lookAtPlayer = lookAtPlayer;
        this.imitatePlayer = imitatePlayer;
        this.usePlayerProfiles = usePlayerProfiles;

        if (profile == null) {
            this.profile = new Profile(
                    UUID.randomUUID(),
                    ProfileUtils.randomName(),
                    Collections.emptyList());
        } else if (profile.getName() == null || profile.getUniqueId() == null) {
            this.profile = new Profile(
                    profile.getUniqueId() == null ? UUID.randomUUID() : profile.getUniqueId(),
                    profile.getName() == null ? ProfileUtils.randomName() : profile.getName(),
                    Collections.emptyList());
        } else {
            this.profile = profile;
        }
        this.gameProfile = ProfileUtils.profileToWrapper(this.profile);
    }

    @NotNull
    public static NPC.Builder builder() {
        return new NPC.Builder();
    }

    protected void show(@NotNull Player player, @NotNull Plugin plugin, long tabListRemoveTicks) {
        this.seeingPlayers.add(player);

        VisibilityModifier modifier = new VisibilityModifier(this);
        modifier.queuePlayerListChange(PlayerInfoAction.ADD_PLAYER).send(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            modifier.queueSpawn().send(player);
            this.spawnCustomizer.handleSpawn(this, player);

            org.bukkit.scoreboard.Scoreboard scoreboard = player.getScoreboard();
            Team npcs = null;
            for(Team team : scoreboard.getTeams()) {
                if(team.getName().equals("npcs-lib")) {
                    npcs = team;
                    break;
                }
            }
            if(npcs == null) {
                npcs = scoreboard.registerNewTeam("npcs-lib");
            }
            npcs.setNameTagVisibility(NameTagVisibility.NEVER);
            npcs.addEntry(profile.getName());

            if (tabListRemoveTicks >= 0) {
                Bukkit.getScheduler().runTaskLater(
                        plugin,
                        () -> modifier.queuePlayerListChange(PlayerInfoAction.REMOVE_PLAYER).send(player),
                        tabListRemoveTicks);
            }

            Bukkit.getPluginManager().callEvent(new PlayerNPCShowEvent(player, this));
        }, 10L);
    }

    protected void hide(
            @NotNull Player player,
            @NotNull Plugin plugin,
            @NotNull PlayerNPCHideEvent.Reason reason
    ) {
        this.visibility()
                .queuePlayerListChange(PlayerInfoAction.REMOVE_PLAYER)
                .queueDestroy()
                .send(player);

        player.getScoreboard().getTeams()
                .stream()
                .filter(team -> team.getName().equals("npcs-lib"))
                .forEach(team -> team.removeEntry(profile.getName()));

        this.removeSeeingPlayer(player);

        Bukkit.getScheduler().runTask(
                plugin,
                () -> Bukkit.getPluginManager().callEvent(new PlayerNPCHideEvent(player, this, reason)));
    }

    protected void removeSeeingPlayer(@NotNull Player player) {
        this.seeingPlayers.remove(player);
    }

    @NotNull
    @Unmodifiable
    public Set<Player> getSeeingPlayers() {
        return (Set<Player>) Collections.unmodifiableCollection(this.seeingPlayers);
    }

    public boolean isShownFor(@NotNull Player player) {
        return this.seeingPlayers.contains(player);
    }

    public void addExcludedPlayer(@NotNull Player player) {
        this.excludedPlayers.add(player);
    }

    public void removeExcludedPlayer(@NotNull Player player) {
        this.excludedPlayers.remove(player);
    }

    @NotNull
    public Collection<Player> getExcludedPlayers() {
        return this.excludedPlayers;
    }

    public boolean isExcluded(@NotNull Player player) {
        return this.excludedPlayers.contains(player);
    }


    @NotNull
    public AnimationModifier animation() {
        return new AnimationModifier(this);
    }

    @NotNull
    public RotationModifier rotation() {
        return new RotationModifier(this);
    }

    @NotNull
    public EquipmentModifier equipment() {
        return new EquipmentModifier(this);
    }

    @NotNull
    public MetadataModifier metadata() {
        return new MetadataModifier(this);
    }

    @NotNull
    public VisibilityModifier visibility() {
        return new VisibilityModifier(this);
    }

    @NotNull
    public LabyModModifier labymod() {
        return new LabyModModifier(this);
    }

    @NotNull
    public TeleportModifier teleport() {
        return new TeleportModifier(this);
    }

    @ApiStatus.Internal
    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @NotNull
    public WrappedGameProfile getGameProfile() {
        return this.gameProfile;
    }

    @NotNull
    public Profile getProfile() {
        return this.profile;
    }

    public int getEntityId() {
        return this.entityId;
    }

    @NotNull
    public Location getLocation() {
        return this.location;
    }

    public boolean isLookAtPlayer() {
        return this.lookAtPlayer;
    }

    public void setLookAtPlayer(boolean lookAtPlayer) {
        this.lookAtPlayer = lookAtPlayer;
    }

    public boolean isImitatePlayer() {
        return this.imitatePlayer;
    }

    public void setImitatePlayer(boolean imitatePlayer) {
        this.imitatePlayer = imitatePlayer;
    }

    public boolean isUsePlayerProfiles() {
        return this.usePlayerProfiles;
    }

    public static class Builder {

        private Profile profile;

        private boolean lookAtPlayer = true;
        private boolean imitatePlayer = true;
        private boolean usePlayerProfiles = false;

        private Location location = new Location(Bukkit.getWorlds().get(0), 0D, 0D, 0D);
        private SpawnCustomizer spawnCustomizer = (npc, player) -> {
        };

        // hologram stuff
        private final ConcurrentLinkedDeque<Object> lines = new ConcurrentLinkedDeque<>();
        //private final Placeholders placeholders = new Placeholders();

        private Builder() {
        }

        @Deprecated
        @ApiStatus.ScheduledForRemoval
        public Builder(@NotNull Profile profile) {
            this.profile = profile;
        }

        public Builder profile(@Nullable Profile profile) {
            this.profile = profile;
            return this;
        }

        public Builder location(@NotNull Location location) {
            this.location = Preconditions.checkNotNull(location, "location");
            return this;
        }

        public Builder lookAtPlayer(boolean lookAtPlayer) {
            this.lookAtPlayer = lookAtPlayer;
            return this;
        }

        public Builder imitatePlayer(boolean imitatePlayer) {
            this.imitatePlayer = imitatePlayer;
            return this;
        }

        public Builder spawnCustomizer(@NotNull SpawnCustomizer spawnCustomizer) {
            this.spawnCustomizer = Preconditions.checkNotNull(spawnCustomizer, "spawnCustomizer");
            return this;
        }

        public Builder usePlayerProfiles(boolean usePlayerProfiles) {
            this.usePlayerProfiles = usePlayerProfiles;
            return this;
        }

        public Builder addLine(@NotNull String line) {
            Validate.notNull(line, "Line cannot be null");
            this.lines.addFirst(line);
            return this;
        }

        public Builder addLine(@NotNull ItemStack item) {
            Validate.notNull(item, "Item cannot be null");
            this.lines.addFirst(item);
            return this;
        }

        /*
        public Builder addPlaceholder(@NotNull String key, @NotNull Function<Player, String> result) {
            Validate.notNull(key, "Placeholder key cannot be null");
            Validate.notNull(result, "Result function cannot be null");
            this.placeholders.add(key, result);
            return this;
        }

         */

        @NotNull
        public NPC build(@NotNull NPCPool pool) {
            if (!this.usePlayerProfiles && (this.profile == null || !this.profile.isComplete())) {
                throw new IllegalArgumentException("No profile given or not completed");
            }

            NPC npc = new NPC(
                    pool.plugin,
                    this.profile,
                    this.location,
                    this.spawnCustomizer,
                    pool.getFreeEntityId(),
                    this.lookAtPlayer,
                    this.imitatePlayer,
                    this.usePlayerProfiles,
                    //this.placeholders,
                    this.lines.toArray());
            pool.takeCareOf(npc);

            return npc;
        }
    }
}