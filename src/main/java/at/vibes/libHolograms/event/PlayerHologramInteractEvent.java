package at.vibes.libHolograms.event;

import at.vibes.libHolograms.other.Hologram;
import at.vibes.libHolograms.other.TextLine;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerHologramInteractEvent extends PlayerHologramEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextLine line;

    public PlayerHologramInteractEvent(
            @NotNull Player player,
            @NotNull Hologram hologram,
            @NotNull TextLine line
    ) {
        super(player, hologram);
        this.line = line;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    public TextLine getLine() {
        return line;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


}