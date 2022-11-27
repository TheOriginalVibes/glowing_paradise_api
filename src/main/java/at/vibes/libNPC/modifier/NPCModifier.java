package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftVersion;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class NPCModifier {

    public static final int MINECRAFT_VERSION = MinecraftVersion.getCurrentVersion().getMinor();

    private final List<LazyPacket> packetContainers = new CopyOnWriteArrayList<>();

    protected NPC npc;

    public NPCModifier(@NotNull NPC npc) {
        this.npc = npc;
    }

    protected void queuePacket(@NotNull LazyPacket packet) {
        this.packetContainers.add(packet);
    }

    protected void queueInstantly(@NotNull LazyPacket packet) {
        PacketContainer container = packet.provide(this.npc, null);
        this.packetContainers.add(($, $1) -> container);
    }

    public void send() {
        this.send(Bukkit.getOnlinePlayers());
    }

    public void send(@NotNull Iterable<? extends Player> players) {
        players.forEach(player -> {
            for (LazyPacket packetContainer : this.packetContainers) {
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(
                            player,
                            packetContainer.provide(this.npc, player));
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        this.packetContainers.clear();
    }

    public void send(@NotNull Player... targetPlayers) {
        this.send(Arrays.asList(targetPlayers));
    }

    @FunctionalInterface
    public interface LazyPacket {
        @NotNull PacketContainer provide(@NotNull NPC targetNpc, @UnknownNullability Player target);
    }
}