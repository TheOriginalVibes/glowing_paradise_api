package at.vibes.libMinecraft.utils;

import at.vibes.libMinecraft.math.Vector2;
import at.vibes.libMinecraft.math.Vector3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class Convert {
    private Convert() {}

    public Vector3 toVector3(Location loc) {
        if (loc == null) {
            return null;
        }
        return new Vector3(loc.getX(), loc.getY(), loc.getZ());
    }

    public Vector3 toBlockVector3(Location loc) {
        if (loc == null) {
            return null;
        }
        return new Vector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public Vector2 toVector2(Location loc) {
        if (loc == null) {
            return null;
        }
        return new Vector2(loc.getX(), loc.getZ());
    }

    public Vector2 toBlockVector2(Location loc) {
        if (loc == null) {
            return null;
        }
        return new Vector2(loc.getBlockX(), loc.getBlockZ());
    }

    public Location toLocation(World world, Vector3 vector) {
        if (world == null || vector == null) {
            return null;
        }
        return new Location(world, vector.x, vector.y, vector.z);
    }

    public Location toLocation(Player player, Vector3 vector) {
        if (player == null || vector == null) {
            return null;
        }
        return toLocation(player.getWorld(), vector);
    }
}