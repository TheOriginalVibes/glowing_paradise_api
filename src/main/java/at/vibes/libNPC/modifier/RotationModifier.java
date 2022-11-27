package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class RotationModifier extends NPCModifier {

    @ApiStatus.Internal
    public RotationModifier(@NotNull NPC npc) {
        super(npc);
    }

    @NotNull
    public RotationModifier queueRotate(float yaw, float pitch) {
        byte yawAngle = (byte) (yaw * 256F / 360F);
        byte pitchAngle = (byte) (pitch * 256F / 360F);

        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.ENTITY_HEAD_ROTATION);
            container.getIntegers().write(0, targetNpc.getEntityId());
            container.getBytes().write(0, yawAngle);

            return container;
        });

        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container;
            if (MINECRAFT_VERSION < 9) {
                container = new PacketContainer(Server.ENTITY_TELEPORT);
                container.getIntegers().write(0, targetNpc.getEntityId());

                Location location = super.npc.getLocation();
                container.getIntegers()
                        .write(1, (int) Math.floor(location.getX() * 32.0D))
                        .write(2, (int) Math.floor(location.getY() * 32.0D))
                        .write(3, (int) Math.floor(location.getZ() * 32.0D));
            } else {
                container = new PacketContainer(Server.ENTITY_LOOK);
                container.getIntegers().write(0, targetNpc.getEntityId());
            }

            container.getBytes()
                    .write(0, yawAngle)
                    .write(1, pitchAngle);
            container.getBooleans().write(0, true);
            return container;
        });

        return this;
    }

    @NotNull
    public RotationModifier queueLookAt(@NotNull Location location) {
        double xDifference = location.getX() - super.npc.getLocation().getX();
        double yDifference = location.getY() - super.npc.getLocation().getY();
        double zDifference = location.getZ() - super.npc.getLocation().getZ();

        double r = Math
                .sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2) + Math.pow(zDifference, 2));

        float yaw = (float) (-Math.atan2(xDifference, zDifference) / Math.PI * 180D);
        yaw = yaw < 0 ? yaw + 360 : yaw;

        float pitch = (float) (-Math.asin(yDifference / r) / Math.PI * 180D);

        return this.queueRotate(yaw, pitch);
    }
}