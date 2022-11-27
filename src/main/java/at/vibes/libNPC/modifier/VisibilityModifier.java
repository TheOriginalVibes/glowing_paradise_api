package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import java.util.ArrayList;
import java.util.Collections;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class VisibilityModifier extends NPCModifier {

    @ApiStatus.Internal
    public VisibilityModifier(@NotNull NPC npc) {
        super(npc);
    }

    @NotNull
    public VisibilityModifier queuePlayerListChange(@NotNull PlayerInfoAction action) {
        return this.queuePlayerListChange(action.handle);
    }

    @NotNull
    public VisibilityModifier queuePlayerListChange(@NotNull EnumWrappers.PlayerInfoAction action) {
        super.queuePacket((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.PLAYER_INFO);
            container.getPlayerInfoAction().write(0, action);

            WrappedGameProfile profile = targetNpc.getGameProfile();

            if (action == EnumWrappers.PlayerInfoAction.ADD_PLAYER && targetNpc.isUsePlayerProfiles()) {
                WrappedGameProfile playerProfile = WrappedGameProfile.fromPlayer(target);

                profile = new WrappedGameProfile(profile.getUUID(), profile.getName());
                profile.getProperties().putAll(playerProfile.getProperties());
            }

            PlayerInfoData data = new PlayerInfoData(
                    profile,
                    20,
                    NativeGameMode.CREATIVE,
                    WrappedChatComponent.fromText(""));
            container.getPlayerInfoDataLists().write(0, new ArrayList<>(Collections.singletonList(data)));
            return container;
        });

        return this;
    }

    @NotNull
    public VisibilityModifier queueSpawn() {
        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.NAMED_ENTITY_SPAWN);
            container.getIntegers().write(0, targetNpc.getEntityId());
            container.getUUIDs().write(0, targetNpc.getProfile().getUniqueId());

            double x = targetNpc.getLocation().getX();
            double y = targetNpc.getLocation().getY();
            double z = targetNpc.getLocation().getZ();

            if (MINECRAFT_VERSION < 9) {
                container.getIntegers()
                        .write(1, (int) Math.floor(x * 32.0D))
                        .write(2, (int) Math.floor(y * 32.0D))
                        .write(3, (int) Math.floor(z * 32.0D));
            } else {
                container.getDoubles()
                        .write(0, x)
                        .write(1, y)
                        .write(2, z);
            }

            container.getBytes()
                    .write(0, (byte) (super.npc.getLocation().getYaw() * 256F / 360F))
                    .write(1, (byte) (super.npc.getLocation().getPitch() * 256F / 360F));
            if (MINECRAFT_VERSION < 15) {
                container.getDataWatcherModifier().write(0, new WrappedDataWatcher());
            }

            return container;
        });

        return this;
    }

    @NotNull
    public VisibilityModifier queueDestroy() {
        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.ENTITY_DESTROY);
            if (MINECRAFT_VERSION >= 17) {
                container.getIntLists()
                        .write(0, new ArrayList<>(Collections.singletonList(targetNpc.getEntityId())));
            } else {
                container.getIntegerArrays().write(0, new int[]{super.npc.getEntityId()});
            }
            return container;
        });

        return this;
    }

    public enum PlayerInfoAction {
        ADD_PLAYER(EnumWrappers.PlayerInfoAction.ADD_PLAYER),
        UPDATE_GAME_MODE(EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE),
        UPDATE_LATENCY(EnumWrappers.PlayerInfoAction.UPDATE_LATENCY),
        UPDATE_DISPLAY_NAME(EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME),
        REMOVE_PLAYER(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);

        private final EnumWrappers.PlayerInfoAction handle;

        PlayerInfoAction(EnumWrappers.PlayerInfoAction handle) {
            this.handle = handle;
        }
    }
}