package at.vibes.libBlocks.Neighbors;

import java.util.Arrays;
import java.util.List;

import at.vibes.libBlocks.LibBlocks;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Neighbors {

    public List<Block> getNeighbors (Block b, BlockFace[] faceList) {
        return Arrays.stream(faceList).map(b::getRelative).toList();
    }

    public List<Block> getDirectAndDiagonalNeighbors (Block b) {
        return getNeighbors(b, LibBlocks.getInstance().getBlockNeighborsGroups().ALL_DIRECT_FACES_AND_DIAGONALS);
    }

    public List<Block> getDirectNeighbors (Block b) {
        return getNeighbors(b, LibBlocks.getInstance().getBlockNeighborsGroups().ALL_DIRECT_FACES);
    }

    public List<Block> getCardinalNeighbors (Block b) {
        return getNeighbors(b, LibBlocks.getInstance().getBlockNeighborsGroups().ALL_CARDINAL_FACES);
    }

    public List<Block> getDirectNeighborsExceptUp (Block b) {
        return getNeighbors(b, LibBlocks.getInstance().getBlockNeighborsGroups().ALL_DIRECT_FACES_EXCEPT_UP);
    }

    public List<Block> getDirectNeighborsExceptDown (Block b) {
        return getNeighbors(b, LibBlocks.getInstance().getBlockNeighborsGroups().ALL_DIRECT_FACES_EXCEPT_DOWN);
    }
}
