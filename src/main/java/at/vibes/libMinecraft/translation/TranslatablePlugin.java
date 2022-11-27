package at.vibes.libMinecraft.translation;

import org.bukkit.plugin.Plugin;

public interface TranslatablePlugin extends Plugin {

    Translation getTranslation();
    void setTranslation(Translation translation);

}