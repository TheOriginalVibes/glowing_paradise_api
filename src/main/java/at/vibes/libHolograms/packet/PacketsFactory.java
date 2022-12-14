package at.vibes.libHolograms.packet;

import at.vibes.libHolograms.util.VersionUtil;
import org.jetbrains.annotations.NotNull;

public class PacketsFactory {

    private static final IPackets instance;

    static {
        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            instance = new IPackets.PacketsV1_8();
        } else if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_9, VersionUtil.VersionEnum.V1_18)) {
            instance = new IPackets.PacketsV1_9V1_18();
        } else {
            instance = new IPackets.PacketsV1_19();
        }
    }

    @NotNull
    public static IPackets get() {
        return instance;
    }
}