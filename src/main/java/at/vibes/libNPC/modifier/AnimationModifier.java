package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class AnimationModifier extends NPCModifier {

    @ApiStatus.Internal
    public AnimationModifier(@NotNull NPC npc) {
        super(npc);
    }

    @NotNull
    public AnimationModifier queue(@NotNull EntityAnimation entityAnimation) {
        return this.queue(entityAnimation.id);
    }

    @NotNull
    public AnimationModifier queue(int animationId) {
        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.ANIMATION);
            container.getIntegers()
                    .write(0, targetNpc.getEntityId())
                    .write(1, animationId);
            return container;
        });
        return this;
    }

    public enum EntityAnimation {
        SWING_MAIN_ARM(0),
        TAKE_DAMAGE(1),
        LEAVE_BED(2),
        SWING_OFF_HAND(3),
        CRITICAL_EFFECT(4),
        MAGIC_CRITICAL_EFFECT(5);

        private final int id;

        EntityAnimation(int id) {
            this.id = id;
        }
    }
}