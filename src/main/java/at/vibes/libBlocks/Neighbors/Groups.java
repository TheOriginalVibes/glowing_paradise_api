package at.vibes.libBlocks.Neighbors;

import org.bukkit.block.BlockFace;

public class Groups {

    public final BlockFace[] ALL_DIRECT_FACES_AND_DIAGONALS = {
            BlockFace.DOWN,
            BlockFace.EAST,
            BlockFace.NORTH_EAST,
            BlockFace.NORTH,
            BlockFace.NORTH_WEST,
            BlockFace.WEST,
            BlockFace.SOUTH_EAST,
            BlockFace.SOUTH,
            BlockFace.SOUTH_WEST,
            BlockFace.UP,
    };

    public final BlockFace[] ALL_DIRECT_FACES = {
            BlockFace.DOWN,
            BlockFace.EAST,
            BlockFace.NORTH,
            BlockFace.WEST,
            BlockFace.SOUTH,
            BlockFace.UP,
    };

    public final BlockFace[] ALL_CARDINAL_FACES = {
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST,
            BlockFace.NORTH,
    };

    public final BlockFace[] ALL_DIRECT_FACES_EXCEPT_UP = {
            BlockFace.DOWN,
            BlockFace.EAST,
            BlockFace.NORTH,
            BlockFace.WEST,
            BlockFace.SOUTH,
    };

    public final BlockFace[] ALL_DIRECT_FACES_EXCEPT_DOWN = {
            BlockFace.UP,
            BlockFace.EAST,
            BlockFace.NORTH,
            BlockFace.WEST,
            BlockFace.SOUTH,
    };
}
