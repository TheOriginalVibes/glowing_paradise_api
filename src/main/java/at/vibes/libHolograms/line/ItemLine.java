package at.vibes.libHolograms.line;

import at.vibes.libHolograms.other.AbstractLine;
import at.vibes.libHolograms.other.Hologram;
import at.vibes.libHolograms.packet.PacketContainerSendable;
import at.vibes.libHolograms.packet.PacketsFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public class ItemLine extends AbstractLine<ItemStack> {

    private final PacketContainerSendable entityMetadataPacket;

    public ItemLine(@NotNull Hologram hologram, @NotNull ItemStack obj) {
        super(hologram, obj);
        entityMetadataPacket = PacketsFactory.get().metadataPacket(entityID);
    }

    @Override
    protected void show(@NotNull Player player) {
        super.show(player);
        entityMetadataPacket.send(player);
        this.update(player);
    }

    @Override
    protected void update(@NotNull Player player) {
        PacketsFactory.get()
                .equipmentPacket(entityID, obj)
                .send(player);
    }

    @Override
    @NotNull
    @Unmodifiable
    public ItemStack get() {
        return obj.clone();
    }
}