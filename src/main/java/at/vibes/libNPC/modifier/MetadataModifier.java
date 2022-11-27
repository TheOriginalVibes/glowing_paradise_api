package at.vibes.libNPC.modifier;

import at.vibes.libNPC.utils.NPC;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MetadataModifier extends NPCModifier {

    private final List<WrappedWatchableObject> metadata = new ArrayList<>();

    @ApiStatus.Internal
    public MetadataModifier(@NotNull NPC npc) {
        super(npc);
    }

    @NotNull
    public <I, O> MetadataModifier queue(@NotNull EntityMetadata<I, O> metadata, @NotNull I value) {
        if (!metadata.getAvailabilitySupplier().get()) {
            return this;
        }

        for (EntityMetadata<I, Object> relatedMetadata : metadata.getRelatedMetadata()) {
            if (!relatedMetadata.getAvailabilitySupplier().get()) {
                continue;
            }
            this.queue(relatedMetadata.getIndex(), relatedMetadata.getMapper().apply(value),
                    relatedMetadata.getOutputType());
        }
        return this
                .queue(metadata.getIndex(), metadata.getMapper().apply(value), metadata.getOutputType());
    }

    @NotNull
    public <T> MetadataModifier queue(int index, @NotNull T value, @NotNull Class<T> clazz) {
        return this
                .queue(index, value, MINECRAFT_VERSION < 9 ? null : WrappedDataWatcher.Registry.get(clazz));
    }

    @NotNull
    public <T> MetadataModifier queue(
            int index,
            @NotNull T value,
            @Nullable WrappedDataWatcher.Serializer serializer
    ) {
        this.metadata.add(serializer == null
                ? new WrappedWatchableObject(index, value)
                : new WrappedWatchableObject(
                new WrappedDataWatcher.WrappedDataWatcherObject(index, serializer), value));
        return this;
    }

    @Override
    public void send(@NotNull Iterable<? extends Player> players) {
        super.queueInstantly((targetNpc, target) -> {
            PacketContainer container = new PacketContainer(Server.ENTITY_METADATA);
            container.getIntegers().write(0, targetNpc.getEntityId());
            container.getWatchableCollectionModifier().write(0, this.metadata);
            return container;
        });
        super.send(players);
    }

    public static class EntityMetadata<I, O> {

        @SuppressWarnings("unchecked")
        public static final EntityMetadata<Boolean, Byte> SNEAKING = new EntityMetadata<>(
                0,
                Byte.class,
                Collections.emptyList(),
                input -> (byte) (input ? 0x02 : 0),

                new EntityMetadata<>(
                        6,
                        (Class<Object>) EnumWrappers.getEntityPoseClass(),
                        Collections.emptyList(),
                        input -> (input ? EnumWrappers.EntityPose.CROUCHING : EnumWrappers.EntityPose.STANDING)
                                .toNms(),
                        () -> NPCModifier.MINECRAFT_VERSION >= 14));

        public static final EntityMetadata<Boolean, Byte> SKIN_LAYERS = new EntityMetadata<>(
                10,
                Byte.class,
                Arrays.asList(9, 9, 10, 14, 14, 15, 17),
                input -> (byte) (input ? 0xff : 0));

        @SuppressWarnings("unchecked")
        public static final EntityMetadata<EnumWrappers.EntityPose, Object> POSE = new EntityMetadata<>(
                6,
                (Class<Object>) EnumWrappers.getEntityPoseClass(),
                Collections.emptyList(),
                EnumWrappers.EntityPose::toNms,
                () -> NPCModifier.MINECRAFT_VERSION >= 14);

        private final int baseIndex;

        private final Class<O> outputType;
        private final Function<I, O> mapper;
        private final Collection<Integer> shiftVersions;
        private final Supplier<Boolean> availabilitySupplier;
        private final Collection<EntityMetadata<I, Object>> relatedMetadata;

        @SafeVarargs
        public EntityMetadata(int baseIndex, Class<O> outputType, Collection<Integer> shiftVersions,
                              Function<I, O> mapper, Supplier<Boolean> availabilitySupplier,
                              EntityMetadata<I, Object>... relatedMetadata) {
            this.baseIndex = baseIndex;
            this.outputType = outputType;
            this.shiftVersions = shiftVersions;
            this.mapper = mapper;
            this.availabilitySupplier = availabilitySupplier;
            this.relatedMetadata = Arrays.asList(relatedMetadata);
        }

        @SafeVarargs
        public EntityMetadata(int baseIndex, Class<O> outputType, Collection<Integer> shiftVersions,
                              Function<I, O> mapper, EntityMetadata<I, Object>... relatedMetadata) {
            this(baseIndex, outputType, shiftVersions, mapper, () -> true, relatedMetadata);
        }

        public int getIndex() {
            return this.baseIndex + Math.toIntExact(
                    this.shiftVersions.stream().filter(minor -> NPCModifier.MINECRAFT_VERSION >= minor)
                            .count());
        }

        @NotNull
        public Class<O> getOutputType() {
            return this.outputType;
        }

        @NotNull
        public Function<I, O> getMapper() {
            return this.mapper;
        }

        @NotNull
        public Supplier<Boolean> getAvailabilitySupplier() {
            return this.availabilitySupplier;
        }

        @NotNull
        public Collection<EntityMetadata<I, Object>> getRelatedMetadata() {
            return this.relatedMetadata;
        }
    }
}