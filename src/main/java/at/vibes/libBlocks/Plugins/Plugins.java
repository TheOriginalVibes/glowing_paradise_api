package at.vibes.libBlocks.Plugins;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugins {

    public JavaPlugin getInstanceByName(String pluginName) {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);

        if (plugin == null || !(plugin instanceof JavaPlugin)) {
            throw new RuntimeException("'"+pluginName+"' not found.");
        }

        return ((JavaPlugin) plugin);
    }
}

