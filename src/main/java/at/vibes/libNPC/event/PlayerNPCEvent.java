package at.vibes.libNPC.event;

import at.vibes.libNPC.modifier.NPCModifier;
import at.vibes.libNPC.utils.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerNPCEvent extends PlayerEvent {
    private final NPC npc;

    public PlayerNPCEvent(Player who, @NotNull NPC npc) {
        super(who);
        this.npc = npc;
    }

    public void send(NPCModifier... npcModifiers) {
        for (NPCModifier npcModifier : npcModifiers) {
            npcModifier.send(super.getPlayer());
        }
    }

    public NPC getNPC() {
        return this.npc;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
}
