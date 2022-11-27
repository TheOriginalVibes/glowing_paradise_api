package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class LabyModModifier extends NPCModifier {
    public static final MinecraftKey LABYMOD_PLUGIN_CHANNEL = new MinecraftKey("labymod3", "main");

    @ApiStatus.Internal
    public LabyModModifier(@NotNull NPC npc) {
        super(npc);
    }

    @NotNull
    public LabyModModifier queue(@NotNull LabyModAction action, int playbackIdentifier) {
        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.CUSTOM_PAYLOAD);

            if (MINECRAFT_VERSION >= 13) {
                container.getMinecraftKeys().write(0, LABYMOD_PLUGIN_CHANNEL);
            } else {
                container.getStrings().write(0, LABYMOD_PLUGIN_CHANNEL.getFullKey());
            }

            ByteBuf content = this.createContent(action, playbackIdentifier);
            if (MinecraftReflection.is(MinecraftReflection.getPacketDataSerializerClass(), content)) {
                container.getModifier().withType(ByteBuf.class).write(0, content);
            } else {
                Object serializer = MinecraftReflection.getPacketDataSerializer(content);
                container.getModifier().withType(ByteBuf.class).write(0, serializer);
            }
            return container;
        });
        return this;
    }

    @NotNull
    protected ByteBuf createContent(@NotNull LabyModAction action, int playbackIdentifier) {
        ByteBuf byteBuf = Unpooled.buffer();
        this.writeString(byteBuf, action.messageKey);

        JsonArray array = new JsonArray();
        JsonObject data = new JsonObject();

        data.addProperty("uuid", super.npc.getProfile().getUniqueId().toString());
        data.addProperty(action.objectPropertyName, playbackIdentifier);

        array.add(data);
        this.writeString(byteBuf, array.toString());

        return byteBuf;
    }

    protected void writeString(@NotNull ByteBuf byteBuf, @NotNull String string) {
        byte[] values = string.getBytes(StandardCharsets.UTF_8);
        this.writeVarInt(byteBuf, values.length);
        byteBuf.writeBytes(values);
    }

    protected void writeVarInt(@NotNull ByteBuf byteBuf, int value) {
        while ((value & -128) != 0) {
            byteBuf.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        byteBuf.writeByte(value);
    }

    public enum LabyModAction {
        EMOTE("emote_api", "emote_id"),
        STICKER("sticker_api", "sticker_id");

        private final String messageKey;
        private final String objectPropertyName;

        LabyModAction(String messageKey, String objectPropertyName) {
            this.messageKey = messageKey;
            this.objectPropertyName = objectPropertyName;
        }

        @NotNull
        public String getMessageKey() {
            return this.messageKey;
        }

        @NotNull
        public String getObjectPropertyName() {
            return this.objectPropertyName;
        }
    }
}