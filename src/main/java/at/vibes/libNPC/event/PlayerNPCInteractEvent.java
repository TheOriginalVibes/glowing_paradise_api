package at.vibes.libNPC.event;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class PlayerNPCInteractEvent extends PlayerNPCEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final EntityUseAction action;
    private final Hand hand;

    public PlayerNPCInteractEvent(
            @NotNull Player who,
            @NotNull NPC npc,
            @NotNull EnumWrappers.EntityUseAction action) {
        this(who, npc, EntityUseAction.fromHandle(action), Hand.MAIN_HAND);
    }

    public PlayerNPCInteractEvent(
            @NotNull Player who,
            @NotNull NPC npc,
            @NotNull EnumWrappers.EntityUseAction action,
            @NotNull EnumWrappers.Hand hand) {
        this(who, npc, EntityUseAction.fromHandle(action), Hand.fromHandle(hand));
    }

    public PlayerNPCInteractEvent(
            @NotNull Player who,
            @NotNull NPC npc,
            @NotNull EntityUseAction action) {
        this(who, npc, action, Hand.MAIN_HAND);
    }

    public PlayerNPCInteractEvent(
            @NotNull Player who,
            @NotNull NPC npc,
            @NotNull EntityUseAction action,
            @NotNull Hand hand) {
        super(who, npc);
        this.action = action;
        this.hand = hand;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public EnumWrappers.EntityUseAction getAction() {
        return this.action.handle;
    }

    @NotNull
    public EntityUseAction getUseAction() {
        return this.action;
    }

    @NotNull
    public Hand getHand() {
        return this.hand;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public enum EntityUseAction {
        INTERACT(EnumWrappers.EntityUseAction.INTERACT),
        ATTACK(EnumWrappers.EntityUseAction.ATTACK),
        INTERACT_AT(EnumWrappers.EntityUseAction.INTERACT_AT);

        private static final EntityUseAction[] VALUES = values();
        private final EnumWrappers.EntityUseAction handle;

        EntityUseAction(EnumWrappers.EntityUseAction handle) {
            this.handle = handle;
        }

        @NotNull
        private static EntityUseAction fromHandle(@NotNull EnumWrappers.EntityUseAction action) {
            for (EntityUseAction value : VALUES) {
                if (value.handle == action) {
                    return value;
                }
            }
            throw new IllegalArgumentException("No use action for handle: " + action);
        }
    }

    public enum Hand {
        MAIN_HAND(EnumWrappers.Hand.MAIN_HAND),
        OFF_HAND(EnumWrappers.Hand.OFF_HAND);

        private static final Hand[] VALUES = values();
        private final EnumWrappers.Hand handle;

        Hand(EnumWrappers.Hand handle) {
            this.handle = handle;
        }

        @NotNull
        private static Hand fromHandle(@NotNull EnumWrappers.Hand hand) {
            for (Hand value : VALUES) {
                if (value.handle == hand) {
                    return value;
                }
            }
            throw new IllegalArgumentException("No hand for handle: " + hand);
        }
    }
}