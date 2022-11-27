package at.vibes.libBlocks.Worlds;

import org.bukkit.World;
import org.bukkit.block.Block;

public class Worlds {

    public boolean isInOverworld (Block b) {
        return b.getWorld().getEnvironment().equals(World.Environment.NORMAL);
    }

    public boolean isInNether (Block b) {
        return b.getWorld().getEnvironment().equals(World.Environment.NETHER);
    }

    public boolean isInTheEnd (Block b) {
        return b.getWorld().getEnvironment().equals(World.Environment.THE_END);
    }

    public boolean isInCustomWorld (Block b) {
        return b.getWorld().getEnvironment().equals(World.Environment.CUSTOM);
    }
}

