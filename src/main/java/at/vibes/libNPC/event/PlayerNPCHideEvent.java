package at.vibes.libNPC.event;

import at.vibes.libNPC.utils.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerNPCHideEvent extends PlayerNPCEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Reason reason;

    public PlayerNPCHideEvent(Player who, NPC npc, Reason reason) {
        super(who, npc);
        this.reason = reason;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    public Reason getReason() {
        return this.reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public enum Reason {
        EXCLUDED,
        SPAWN_DISTANCE,
        UNLOADED_CHUNK,
        REMOVED,
        RESPAWNED
    }
}