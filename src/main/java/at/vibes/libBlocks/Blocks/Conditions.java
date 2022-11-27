package at.vibes.libBlocks.Blocks;

import org.bukkit.block.Block;

public class Conditions {

    public boolean areSameType (Block a, Block b) {
        return a.getType().equals(b.getType());
    }

    public boolean isHigherThan (Block a, Block b) {
        return a.getY() > b.getY();
    }

    public boolean isLowerThan (Block a, Block b) {
        return a.getY() < b.getY();
    }

    public boolean isSolid (Block b) {
        return b.getType().isSolid();
    }
}

