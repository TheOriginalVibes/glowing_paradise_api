package at.vibes.libHolograms.event;

import at.vibes.libHolograms.other.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerHologramEvent extends PlayerEvent {

    private final Hologram hologram;

    public PlayerHologramEvent(@NotNull Player player, @NotNull Hologram hologram) {
        super(player);
        this.hologram = hologram;
    }

    @NotNull
    public Hologram getHologram() {
        return hologram;
    }

}