package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class TeleportModifier extends NPCModifier {

    @ApiStatus.Internal
    public TeleportModifier(@NotNull NPC npc) {
        super(npc);
    }

    @NotNull
    public TeleportModifier queueTeleport(@NotNull Location location, boolean onGround) {
        byte yawAngle = this.getCompressedAngle(location.getYaw());
        byte pitchAngle = this.getCompressedAngle(location.getPitch());
        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.ENTITY_TELEPORT);
            container.getIntegers().write(0, targetNpc.getEntityId());
            if (MINECRAFT_VERSION < 9) {
                container.getIntegers().write(1, (int) Math.floor(location.getX() * 32.0D));
                container.getIntegers().write(2, (int) Math.floor(location.getY() * 32.0D));
                container.getIntegers().write(3, (int) Math.floor(location.getZ() * 32.0D));
            } else {
                container.getDoubles().write(0, location.getX());
                container.getDoubles().write(1, location.getY());
                container.getDoubles().write(2, location.getZ());
            }
            container.getBytes().write(0, yawAngle);
            container.getBytes().write(1, pitchAngle);
            container.getBooleans().write(0, onGround);

            super.npc.setLocation(location);
            return container;
        });
        return this;
    }

    @NotNull
    public TeleportModifier queueTeleport(@NotNull Location location) {
        return this.queueTeleport(location, false);
    }

    private byte getCompressedAngle(double angle) {
        return (byte) (angle * 256F / 360F);
    }
}