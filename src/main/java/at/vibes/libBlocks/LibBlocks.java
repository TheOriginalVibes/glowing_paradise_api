package at.vibes.libBlocks;

import at.vibes.libBlocks.Blocks.*;
import at.vibes.libBlocks.Fluids.Constants;
import at.vibes.libBlocks.Fluids.Fluids;
import at.vibes.libBlocks.Neighbors.Groups;
import at.vibes.libBlocks.Neighbors.Neighbors;
import at.vibes.libBlocks.Players.GameModes;
import at.vibes.libBlocks.Plugins.Plugins;
import at.vibes.libBlocks.Worlds.Worlds;
import org.bukkit.plugin.Plugin;

public abstract class LibBlocks {
    private static LibBlocks instance;

    LibBlocks() {instance = this;}

    //Biomes
    public abstract at.vibes.libBlocks.Biomes.Categories getBiomesCategories();
    //Blocks
    public abstract ActionList getActionList();
    public abstract BulkActionList getBulkActionList();
    public abstract BulkActionList getBulkActionList(long t, long d, Plugin p);
    public abstract Categories getBlockCategories();
    public abstract Changes getBlockChanges();
    public abstract Conditions getBlockConditions();
    //Entities
    public abstract at.vibes.libBlocks.Entities.Categories getEntityCategories();
    //Fluids
    public abstract Constants getFluidConstants();
    public abstract Fluids getFluids();
    //Neighbors
    public abstract Groups getBlockNeighborsGroups();
    public abstract Neighbors getBlockNeighbors();
    //Players
    public abstract at.vibes.libBlocks.Players.Constants getPlayerConstants();
    public abstract GameModes getPlayerGameModes();
    //Plugins
    public abstract Plugins getPlugins();
    //Weather
    public abstract at.vibes.libBlocks.Weather.Constants getWeatherConstants();
    public abstract at.vibes.libBlocks.Weather.Conditions getWeatherConditions();
    //World
    public abstract Worlds getWorlds();


    public static LibBlocks getInstance() {return instance;}
}
