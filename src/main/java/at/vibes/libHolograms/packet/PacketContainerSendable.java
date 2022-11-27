package at.vibes.libHolograms.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PacketContainerSendable extends PacketContainer {

    public PacketContainerSendable(PacketType type) {
        super(type);
    }

    public void send(@NotNull Player player) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, this);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}