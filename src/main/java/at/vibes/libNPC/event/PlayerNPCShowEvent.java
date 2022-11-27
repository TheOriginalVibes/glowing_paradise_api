package at.vibes.libNPC.event;

import at.vibes.libNPC.utils.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerNPCShowEvent extends PlayerNPCEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerNPCShowEvent(Player who, NPC npc) {
        super(who, npc);
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}