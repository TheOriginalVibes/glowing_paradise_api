package at.vibes.libBlocks.Fluids;

import java.util.Optional;

import at.vibes.libBlocks.Blocks.Categories;
import at.vibes.libBlocks.LibBlocks;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;

import static at.vibes.libBlocks.Fluids.Constants.*;

public class Fluids {

    public Optional<Integer> getLowestAllowedFlowingLevel (Block b) {
        switch (b.getType()) {
            case WATER -> {
                return Optional.of(LOWEST_FLOWING_WATER_LEVEL);
            }
            case LAVA -> {
                if (b.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                    return Optional.of(LOWEST_FLOWING_FLUID_LEVEL);
                } else {
                    return Optional.of(LOWEST_OVERWORLD_FLOWING_LAVA_LEVEL);
                }
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    public boolean isAFlowingFluidLevel (int l) {
        return LOWEST_FLOWING_FLUID_LEVEL >= l && l > 0;
    }

    public boolean isAFallingFluidLevel (int l) {
        return LOWEST_FALLING_FLUID_LEVEL >= l && l > LOWEST_FLOWING_FLUID_LEVEL;
    }

    public boolean isASourceFluidLevel (int l) {
        return l == 0;
    }

    public boolean isAFluidLevel (int level) {
        return LOWEST_FALLING_FLUID_LEVEL >= level;
    }

    public boolean canBeFlowedToward (Block b) {
        return b.isPassable() && !b.isLiquid() && !LibBlocks.getInstance().getBlockCategories().isAnAquaticPlant(b);
    }

    public boolean canFlowToward (Block to, Block from) {
        return isAFluidBlock(from) && canBeFlowedToward(to);
    }

    public boolean isASourceBlock (Block b) {
        return b.getBlockData() instanceof Levelled l && l.getLevel() == 0;
    }

    public boolean isAFlowingBlock (Block b) {
        return b.isLiquid() && !isASourceBlock(b);
    }

    public boolean isAWaterBlock (Block b) {
        return b.getType().equals(Material.WATER);
    }

    public boolean isALavaBlock (Block b) {
        return b.getType().equals(Material.LAVA);
    }

    public boolean isAFluidBlock (Block b) {
        return b.isLiquid();
    }

    public boolean isALevelledBlock (Block b) {
        return b.getBlockData() instanceof Levelled;
    }

    public boolean isWaterloggable (Block b) {
        return b.getBlockData() instanceof Waterlogged;
    }

    public boolean isWaterlogged (Block b) {
        if (b.getBlockData() instanceof Waterlogged wl) {
            return wl.isWaterlogged();
        } else {
            return false;
        }
    }

    public boolean setWaterlogged (Block b, boolean v) {
        if (b.getBlockData() instanceof Waterlogged wl) {
            wl.setWaterlogged(v);

            b.setBlockData(wl);

            return true;
        } else {
            return false;
        }
    }

    public boolean isFullerThan (Block a, Block b) {
        return getFluidLevel(a).orElse(LOWEST_FALLING_FLUID_LEVEL) < getFluidLevel(b).orElse(LOWEST_FALLING_FLUID_LEVEL);
    }

    public boolean setFluidLevel (Block b, int v) {
        if (b.getBlockData() instanceof Levelled l && isAFluidLevel(v)) {
            l.setLevel(v);

            b.setBlockData(l);

            return true;
        }

        return false;
    }

    public Optional<Integer> getFluidLevel (Block b) {
        if (b.getBlockData() instanceof Levelled l) {
            return Optional.of(l.getLevel());
        } else {
            return Optional.empty();
        }
    }
}
