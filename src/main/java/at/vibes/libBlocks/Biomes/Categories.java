package at.vibes.libBlocks.Biomes;

import org.bukkit.block.Block;

public class Categories {

    public boolean isAnAcaciaGeneratingBiome (Block b) {
        return switch (b.getBiome()) {
            case SAVANNA -> true;
            case SAVANNA_PLATEAU -> true;
            default -> false;
        };
    }

    public boolean isAnOakGeneratingBiome (Block b) {
        return switch (b.getBiome()) {
            case BAMBOO_JUNGLE -> true;
            case DARK_FOREST -> true;
            case FOREST -> true;
            case JUNGLE -> true;
            case PLAINS -> true;
            case RIVER -> true;
            case SAVANNA -> true;
            case SWAMP -> true;
            default -> false;
        };
    }

    public boolean isInASwamp (Block b) {
        return switch (b.getBiome()) {
            case SWAMP -> true;
            default -> false;
        };
    }

    public boolean isInARainlessBiome (Block b) {
        return switch (b.getBiome()) {
            case DESERT -> true;
            case SAVANNA -> true;
            case SAVANNA_PLATEAU -> true;
            case BADLANDS -> true;
            default -> false;
        };
    }

    public boolean isInAFreshwaterBiome (Block b) {
        return switch (b.getBiome()) {
            case RIVER -> true;
            case SWAMP -> true;
            case FROZEN_RIVER -> true;
            default -> false;
        };
    }

    public boolean isInAnOceanBiome (Block b) {
        return switch (b.getBiome()) {
            case BEACH -> true;
            case OCEAN -> true;
            case COLD_OCEAN -> true;
            case DEEP_COLD_OCEAN -> true;
            case DEEP_LUKEWARM_OCEAN -> true;
            case DEEP_OCEAN -> true;
            case LUKEWARM_OCEAN -> true;
            case SNOWY_BEACH -> true;
            case WARM_OCEAN -> true;
            case FROZEN_OCEAN -> true;
            default -> false;
        };
    }

    public boolean isInAWaterBiome (Block b) {
        return isInAnOceanBiome(b) || isInAFreshwaterBiome(b);
    }
}
