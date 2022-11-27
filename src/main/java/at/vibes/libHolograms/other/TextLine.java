package at.vibes.libHolograms.other;

import at.vibes.libHolograms.packet.PacketsFactory;
import at.vibes.libHolograms.util.AABB;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TextLine extends AbstractLine<String> {

    private final boolean clickable;
    protected AABB hitbox;
    private boolean isEmpty;

    public TextLine(@NotNull Hologram hologram, @NotNull String obj, boolean clickable) {
        super(hologram, obj);
        this.clickable = clickable;
    }

    @Override
    protected void show(@NotNull Player player) {
        isEmpty = obj.isEmpty();
        if (!isEmpty) {
            super.show(player);
            PacketsFactory.get()
                    .metadataPacket(entityID, obj, player, hologram.getPlaceholders(), true, true)
                    .send(player);
        }
    }

    @Override
    protected void update(@NotNull Player player) {
        byte spawnBefore = (byte) ((isEmpty ? 1 : 0) | (obj.isEmpty() ? 1 : 0) << 1);
    /*
      0x00  = is already showed
      0x01  = is hided but now has changed
      0x02  = is already showed but is empty
      0x03  = is hided and isn't changed
     */
        switch (spawnBefore) {
            case 0x03:
                break;
            case 0x02:
                super.hide(player);
                isEmpty = true;
                break;
            case 0x01:
                super.show(player);
                isEmpty = false;
            case 0x00:
                PacketsFactory.get()
                        .metadataPacket(entityID, obj, player, hologram.getPlaceholders(), spawnBefore == 0x01,
                                spawnBefore == 0x01)
                        .send(player);
        }


    }

    @Override
    @NotNull
    public String get() {
        return obj;
    }

    @NotNull
    public String parse(Player player) {
        return hologram.getPlaceholders().parse(obj, player);
    }

    @Override
    protected void setLocation(@NotNull Location location) {
        super.setLocation(location);
        if (clickable) {
            double chars = obj.length();
            double size = 0.105;
            double dist = size * (chars / 2d);

            hitbox = new AABB(
                    new AABB.Vec3D(-dist, -0.039, -dist),
                    new AABB.Vec3D(dist, +0.039, dist));
            hitbox.translate(AABB.Vec3D.fromLocation(location.clone().add(0, 1.40, 0)));
        }
    }

    public boolean isClickable() {
        return clickable;
    }
}