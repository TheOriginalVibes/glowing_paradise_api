package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import java.util.Collections;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class EquipmentModifier extends NPCModifier {

    public static final int MAINHAND = 0;
    public static final int OFFHAND = 1;
    public static final int FEET = 2;
    public static final int LEGS = 3;
    public static final int CHEST = 4;
    public static final int HEAD = 5;

    private static final EnumWrappers.ItemSlot[] ITEM_SLOTS = EnumWrappers.ItemSlot.values();

    @ApiStatus.Internal
    public EquipmentModifier(@NotNull NPC npc) {
        super(npc);
    }

    @NotNull
    public EquipmentModifier queue(
            @NotNull EnumWrappers.ItemSlot itemSlot,
            @NotNull ItemStack equipment
    ) {
        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.ENTITY_EQUIPMENT);
            container.getIntegers().write(0, targetNpc.getEntityId());

            if (MINECRAFT_VERSION < 16) {
                if (MINECRAFT_VERSION < 9) {
                    int slotId = itemSlot.ordinal();
                    if (slotId > 0) {
                        slotId--;
                    }

                    container.getIntegers().write(1, slotId);
                } else {
                    container.getItemSlots().write(0, itemSlot);
                }
                container.getItemModifier().write(0, equipment);
            } else {
                container.getSlotStackPairLists()
                        .write(0, Collections.singletonList(new Pair<>(itemSlot, equipment)));
            }
            return container;
        });
        return this;
    }

    @NotNull
    public EquipmentModifier queue(int itemSlot, @NotNull ItemStack equipment) {
        for (EnumWrappers.ItemSlot slot : ITEM_SLOTS) {
            if (slot.ordinal() == itemSlot) {
                return queue(slot, equipment);
            }
        }

        throw new IllegalArgumentException("Provided itemSlot is invalid");
    }
}