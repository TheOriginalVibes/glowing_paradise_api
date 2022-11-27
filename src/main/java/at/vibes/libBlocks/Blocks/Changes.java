package at.vibes.libBlocks.Blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Changes {

    public void breakAndSetType (Block b, Material t) {
        b.breakNaturally();
        b.setType(t);
    }

    public void copyTo (Block to, Block from) {
        breakAndSetType(to, from.getType());
    }

    public void removeBlock (Block b) {
        b.setType(Material.AIR);
    }

    public void transferTo (Block to, Block from) {
        copyTo(to, from);
        removeBlock(from);
    }

    public void applyGravityTo (Block b) {
        b.getWorld().spawnFallingBlock(b.getLocation().add(0.5, 0, 0.5), b.getBlockData());
        removeBlock(b);
    }
}

