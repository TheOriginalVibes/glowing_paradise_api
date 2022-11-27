package at.vibes.libHolograms.other;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

import at.vibes.libHolograms.event.PlayerHologramHideEvent;
import at.vibes.libHolograms.event.PlayerHologramShowEvent;
import at.vibes.libHolograms.line.ItemLine;
import at.vibes.libHolograms.placeholder.Placeholders;
import at.vibes.libHolograms.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class Hologram {
    protected final List<AbstractLine<?>> lines;
    protected final Set<Player> seeingPlayers = new CopyOnWriteArraySet<>();
    protected final Set<Player> excludedPlayers = new CopyOnWriteArraySet<>();
    private final Plugin plugin;
    private final Placeholders placeholders;
    private Location location;

    @Deprecated
    @ApiStatus.Internal
    public Hologram(
            @NotNull Plugin plugin,
            @NotNull Location location,
            @Nullable Placeholders placeholders,
            @NotNull Object[]... l
    ) {
        this.plugin = plugin;
        this.location = location;
        this.placeholders = placeholders == null ? new Placeholders() : placeholders;

        LinkedList<AbstractLine<?>> tempReversed = new LinkedList<>();
        Location cloned = this.location.clone().subtract(0, 0.28, 0);
        for (int j = 0; j < l.length; j++) {
            Object[] line = l[j];
            double up = 0.28D;
            if (j > 0 && l[j - 1].length == 1 /* ItemStack */) {
                up = 0.0D;
            }
            Object val = line[0];
            if (val instanceof String) {
                TextLine tempLine = new TextLine(this, (String) val, (boolean) line[1]);
                tempLine.setLocation(cloned.add(0.0, up, 0).clone());
                tempReversed.addFirst(tempLine);
            } else if (val instanceof ItemStack) {
                ItemLine tempLine = new ItemLine(this, (ItemStack) val);
                tempLine.setLocation(cloned.add(0.0, 0.60D, 0).clone());
                tempReversed.addFirst(tempLine);
            }
        }
        this.lines = Collections.unmodifiableList(tempReversed);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @ApiStatus.Experimental
    @ApiStatus.AvailableSince("1.2-SNAPSHOT")
    public void teleport(@NotNull Location to) {
        Validate.notNull(to, "Destination cannot be null");
        AbstractLine<?> firstLine = this.lines.get(0);
        this.location = to.clone();

        double baseY = firstLine.getLocation().getY();
        double destY = (this.location.getY() - 0.28D) + (firstLine instanceof TextLine ? 0.28D : 0.60D);

        this.teleportLine(destY, firstLine);
        AbstractLine<?> tempLine;
        for (int j = 1; j < this.lines.size(); j++) {
            tempLine = this.lines.get(j);
            this.teleportLine(destY + Math.abs(baseY - tempLine.getLocation().getY()), tempLine);
        }
    }
    private void teleportLine(double destY, AbstractLine<?> tempLine) {
        Location dest = this.location.clone();
        dest.setY(destY);
        tempLine.setLocation(dest);
        this.seeingPlayers.forEach(tempLine::teleport);
    }

    protected void show(@NotNull Player player) {
        this.seeingPlayers.add(player);
        for (AbstractLine<?> line : this.lines) {
            line.show(player);
        }
        Bukkit.getScheduler().runTask(
                plugin,
                () -> Bukkit.getPluginManager().callEvent(new PlayerHologramShowEvent(player, this)));
    }

    protected void hide(@NotNull Player player) {
        for (AbstractLine<?> line : this.lines) {
            line.hide(player);
        }
        this.seeingPlayers.remove(player);

        Bukkit.getScheduler().runTask(
                plugin,
                () -> Bukkit.getPluginManager().callEvent(new PlayerHologramHideEvent(player, this)));
    }

    @NotNull
    protected Plugin getPlugin() {
        return plugin;
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
    public Set<Player> getExcludedPlayers() {
        return excludedPlayers;
    }

    public boolean isExcluded(@NotNull Player player) {
        return this.excludedPlayers.contains(player);
    }

    @NotNull
    @Unmodifiable
    public List<AbstractLine<?>> getLines() {
        return lines;
    }

    protected void removeSeeingPlayer(Player player) {
        this.seeingPlayers.remove(player);
    }

    @NotNull
    @Unmodifiable
    public Set<Player> getSeeingPlayers() {
        return Collections.unmodifiableSet(seeingPlayers);
    }

    @NotNull
    @Unmodifiable
    public Location getLocation() {
        return location.clone();
    }

    public Placeholders getPlaceholders() {
        return placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hologram hologram = (Hologram) o;
        return Objects.equals(location, hologram.location) && Objects.equals(lines, hologram.lines);
    }

    public static class Builder {

        private static final Object[][] CACHE_ARR = new Object[0][0];

        private final ConcurrentLinkedDeque<Object[]> lines = new ConcurrentLinkedDeque<>();
        private final Placeholders placeholders = new Placeholders();
        private Location location;

        @NotNull
        public Builder addLine(@NotNull String line, boolean clickable) {
            Validate.notNull(line, "Line cannot be null");
            this.lines.addFirst(new Object[]{line, clickable});
            return this;
        }

        @NotNull
        public Builder addLine(@NotNull String line) {
            return addLine(line, false);
        }

        @NotNull
        public Builder addLine(@NotNull ItemStack item) {
            Validate.notNull(item, "Item cannot be null");
            this.lines.addFirst(new Object[]{item});
            return this;
        }

        @NotNull
        public Builder location(@NotNull Location location) {
            Validate.notNull(location, "Location cannot be null");
            this.location = location;
            return this;
        }

        @NotNull
        public Builder addPlaceholder(@NotNull String key, @NotNull Function<Player, String> result) {
            this.placeholders.add(key, result);
            return this;
        }

        @NotNull
        public Hologram build(@NotNull HologramPool pool) {
            if (location == null || lines.isEmpty()) {
                throw new IllegalArgumentException("No location given or not completed");
            }
            Hologram hologram = new Hologram(pool.getPlugin(), this.location, this.placeholders,
                    this.lines.toArray(CACHE_ARR));
            pool.takeCareOf(hologram);
            return hologram;
        }
    }
}