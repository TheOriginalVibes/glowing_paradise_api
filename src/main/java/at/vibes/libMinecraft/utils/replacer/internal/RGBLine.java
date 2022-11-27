package at.vibes.libMinecraft.utils.replacer.internal;

import at.vibes.libMinecraft.utils.replacer.StringReplacer;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;

public class RGBLine implements StringReplacer {
    private static final char HEX_SYMBOL = '#';
    private static final int MAX_HEX_LENGTH = 6;

    @Override
    public String apply(String string) {
        String rgbString = string;
        for (int i = 0; i < rgbString.length(); i++) {
            if (rgbString.charAt(i) == HEX_SYMBOL) {
                final int endIndex = i + MAX_HEX_LENGTH + 1;
                final StringBuilder stringBuilder = new StringBuilder();
                boolean success = true;
                for (int i2 = i; i2 < endIndex; i2++) {
                    if (rgbString.length() - 1 < i2) {
                        success = false;
                        break;
                    }
                    stringBuilder.append(rgbString.charAt(i2));
                }
                if (success) {
                    try {
                        rgbString = rgbString.substring(0, i)
                                + ChatColor.of(stringBuilder.toString())
                                + rgbString.substring(endIndex);
                    } catch (Exception e) {
                        //
                    }
                }
            }
        }
        return rgbString;
    }

    @Override
    public boolean isSupported() {
        return BUKKIT_VERSION > 15;
    }

    public static final int BUKKIT_VERSION = NumberUtils.toInt(getFormattedBukkitPackage());
    public static String getFormattedBukkitPackage() {
        final String version = getBukkitPackage().replace("v", "").replace("R", "");
        return version.substring(2, version.length() - 2);
    }
    public static String getBukkitPackage() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
