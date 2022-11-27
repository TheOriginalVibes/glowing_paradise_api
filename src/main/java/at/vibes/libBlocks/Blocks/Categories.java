package at.vibes.libBlocks.Blocks;

import org.bukkit.block.Block;

public class Categories {

    public boolean isRock (Block b) {
        return switch (b.getType()) {
            case STONE -> true;
            case DEEPSLATE -> true;
            case ANDESITE -> true;
            case GRANITE -> true;
            case DIORITE -> true;
            default -> false;
        };
    }

    public boolean isOre (Block b) {
        return switch (b.getType()) {
            case COAL_ORE -> true;
            case COPPER_ORE -> true;
            case DEEPSLATE_COAL_ORE -> true;
            case DEEPSLATE_COPPER_ORE -> true;
            case DEEPSLATE_DIAMOND_ORE -> true;
            case DEEPSLATE_EMERALD_ORE -> true;
            case DEEPSLATE_GOLD_ORE -> true;
            case DEEPSLATE_IRON_ORE -> true;
            case DEEPSLATE_LAPIS_ORE -> true;
            case DEEPSLATE_REDSTONE_ORE -> true;
            case DIAMOND_ORE -> true;
            case EMERALD_ORE -> true;
            case GOLD_ORE -> true;
            case IRON_ORE -> true;
            case LAPIS_ORE -> true;
            case REDSTONE_ORE -> true;
            case NETHER_GOLD_ORE -> true;
            case NETHER_QUARTZ_ORE -> true;
            default -> false;
        };
    }

    public boolean isSoil (Block b) {
        return switch (b.getType()) {
            case DIRT -> true;
            case GRASS_BLOCK -> true;
            case COARSE_DIRT -> true;
            case PODZOL -> true;
            case MYCELIUM -> true;
            case ROOTED_DIRT -> true;
            case MOSS_BLOCK -> true;
            case FARMLAND -> true;
            default -> false;
        };
    }

    public boolean canFall (Block b) {
        return switch (b.getType()) {
            case SAND -> true;
            case GRAVEL -> true;
            case RED_SAND -> true;
            case LIGHT_BLUE_CONCRETE_POWDER -> true;
            case BLUE_CONCRETE_POWDER -> true;
            case BROWN_CONCRETE_POWDER -> true;
            case CYAN_CONCRETE_POWDER -> true;
            case GRAY_CONCRETE_POWDER -> true;
            case GREEN_CONCRETE_POWDER -> true;
            case LIGHT_GRAY_CONCRETE_POWDER -> true;
            case LIME_CONCRETE_POWDER -> true;
            case MAGENTA_CONCRETE_POWDER -> true;
            case ORANGE_CONCRETE_POWDER -> true;
            case PINK_CONCRETE_POWDER -> true;
            case PURPLE_CONCRETE_POWDER -> true;
            case RED_CONCRETE_POWDER -> true;
            case WHITE_CONCRETE_POWDER -> true;
            case YELLOW_CONCRETE_POWDER -> true;
            case BLACK_CONCRETE_POWDER -> true;
            case SOUL_SAND -> true;
            default -> false;
        };
    }

    public boolean isLeaves (Block b) {
        return switch (b.getType()) {
            case ACACIA_LEAVES -> true;
            case AZALEA_LEAVES -> true;
            case BIRCH_LEAVES -> true;
            case DARK_OAK_LEAVES -> true;
            case FLOWERING_AZALEA_LEAVES -> true;
            case JUNGLE_LEAVES -> true;
            case OAK_LEAVES -> true;
            case SPRUCE_LEAVES -> true;
            default -> false;
        };
    }

    public boolean isGlass (Block b) {
        return switch (b.getType()) {
            case GLASS -> true;
            case GLASS_PANE -> true;
            case BLACK_STAINED_GLASS -> true;
            case BLACK_STAINED_GLASS_PANE -> true;
            case BLUE_STAINED_GLASS -> true;
            case BLUE_STAINED_GLASS_PANE -> true;
            case BROWN_STAINED_GLASS -> true;
            case BROWN_STAINED_GLASS_PANE -> true;
            case CYAN_STAINED_GLASS -> true;
            case CYAN_STAINED_GLASS_PANE -> true;
            case GRAY_STAINED_GLASS -> true;
            case GRAY_STAINED_GLASS_PANE -> true;
            case GREEN_STAINED_GLASS -> true;
            case GREEN_STAINED_GLASS_PANE -> true;
            case LIGHT_BLUE_STAINED_GLASS -> true;
            case LIGHT_BLUE_STAINED_GLASS_PANE -> true;
            case LIGHT_GRAY_STAINED_GLASS -> true;
            case LIGHT_GRAY_STAINED_GLASS_PANE -> true;
            case LIME_STAINED_GLASS -> true;
            case LIME_STAINED_GLASS_PANE -> true;
            case MAGENTA_STAINED_GLASS -> true;
            case MAGENTA_STAINED_GLASS_PANE -> true;
            case ORANGE_STAINED_GLASS -> true;
            case ORANGE_STAINED_GLASS_PANE -> true;
            case PINK_STAINED_GLASS -> true;
            case PINK_STAINED_GLASS_PANE -> true;
            case PURPLE_STAINED_GLASS -> true;
            case PURPLE_STAINED_GLASS_PANE -> true;
            case RED_STAINED_GLASS -> true;
            case RED_STAINED_GLASS_PANE -> true;
            case TINTED_GLASS -> true;
            case WHITE_STAINED_GLASS -> true;
            case WHITE_STAINED_GLASS_PANE -> true;
            case YELLOW_STAINED_GLASS -> true;
            case YELLOW_STAINED_GLASS_PANE -> true;
            default -> false;
        };
    }

    public boolean isCobbled (Block b) {
        return switch (b.getType()) {
            case COBBLESTONE -> true;
            case COBBLESTONE_SLAB -> true;
            case COBBLESTONE_STAIRS -> true;
            case COBBLESTONE_WALL -> true;
            case COBBLED_DEEPSLATE -> true;
            case COBBLED_DEEPSLATE_SLAB -> true;
            case COBBLED_DEEPSLATE_STAIRS -> true;
            case COBBLED_DEEPSLATE_WALL -> true;
            default -> false;
        };
    }

    public boolean isPlanks (Block b) {
        return switch (b.getType()) {
            case ACACIA_PLANKS -> true;
            case BIRCH_PLANKS -> true;
            case CRIMSON_PLANKS -> true;
            case DARK_OAK_PLANKS -> true;
            case JUNGLE_PLANKS -> true;
            case OAK_PLANKS -> true;
            case SPRUCE_PLANKS -> true;
            case WARPED_PLANKS -> true;
            default -> false;
        };
    }

    public boolean isAnAquaticPlant (Block b) {
        return switch (b.getType()) {
            case KELP -> true;
            case KELP_PLANT -> true;
            case SEAGRASS -> true;
            case TALL_SEAGRASS -> true;
            case BRAIN_CORAL -> true;
            case BRAIN_CORAL_FAN -> true;
            case BRAIN_CORAL_WALL_FAN -> true;
            case BUBBLE_CORAL -> true;
            case BUBBLE_CORAL_FAN -> true;
            case BUBBLE_CORAL_WALL_FAN -> true;
            case DEAD_BRAIN_CORAL -> true;
            case DEAD_BRAIN_CORAL_FAN -> true;
            case DEAD_BRAIN_CORAL_WALL_FAN -> true;
            case DEAD_BUBBLE_CORAL -> true;
            case DEAD_BUBBLE_CORAL_FAN -> true;
            case DEAD_BUBBLE_CORAL_WALL_FAN -> true;
            case DEAD_FIRE_CORAL -> true;
            case DEAD_FIRE_CORAL_FAN -> true;
            case DEAD_FIRE_CORAL_WALL_FAN -> true;
            case DEAD_HORN_CORAL -> true;
            case DEAD_HORN_CORAL_FAN -> true;
            case DEAD_HORN_CORAL_WALL_FAN -> true;
            case DEAD_TUBE_CORAL -> true;
            case DEAD_TUBE_CORAL_FAN -> true;
            case DEAD_TUBE_CORAL_WALL_FAN -> true;
            case FIRE_CORAL -> true;
            case FIRE_CORAL_FAN -> true;
            case FIRE_CORAL_WALL_FAN -> true;
            case HORN_CORAL -> true;
            case HORN_CORAL_FAN -> true;
            case HORN_CORAL_WALL_FAN -> true;
            case TUBE_CORAL -> true;
            case TUBE_CORAL_FAN -> true;
            case TUBE_CORAL_WALL_FAN -> true;
            default -> false;
        };
    }
}
