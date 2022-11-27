package at.vibes.libHolograms.event;

import at.vibes.libHolograms.other.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerHologramShowEvent extends PlayerHologramEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerHologramShowEvent(@NotNull Player player, @NotNull Hologram hologram) {
        super(player, hologram);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}