package at.vibes.libBlocks.Weather;

import at.vibes.libBlocks.LibBlocks;
import org.bukkit.World;

import org.bukkit.block.Block;

public class Conditions {

    public boolean isFreezing (Block b) {
        return b.getTemperature() <= LibBlocks.getInstance().getWeatherConstants().MAX_SNOW_TEMPERATURE;
    }

    public boolean hasLightFromSky (Block b) {
        return b.getLightFromSky() != 0;
    }

    public boolean hasSkyAbove (Block b) {
        return b.getWorld().getHighestBlockAt(b.getLocation()).equals(b);
    }

    public boolean canReceiveRainfall (Block b) {
        return hasSkyAbove(b) && !LibBlocks.getInstance().getBiomesCategories().isInARainlessBiome(b);
    }

    public boolean isInRain (Block b) {
        return hasWeather(b) && !isFreezing(b);
    }

    public boolean isInSnow (Block b) {
        return hasWeather(b) && isFreezing(b);
    }

    public boolean isInAStorm (Block b) {
        return canReceiveRainfall(b) && b.getWorld().isThundering();
    }

    public boolean isInASnowstorm (Block b) {
        return isInAStorm(b) && isFreezing(b);
    }

    public boolean isInAThunderstorm (Block b) {
        return isInAStorm(b) && !isFreezing(b);
    }

    public boolean hasWeather (Block b) {
        return canReceiveRainfall(b) && !b.getWorld().isClearWeather();
    }

    public boolean isNightTime (Block block) {
        World world = block.getWorld();
        return LibBlocks.getInstance().getWeatherConstants().NIGHT_START_TICKS < world.getTime() && world.getTime() < LibBlocks.getInstance().getWeatherConstants().NIGHT_END_TICKS;
    }

    public boolean isDayTime (Block block) {
        return !isNightTime(block);
    }

    public boolean hasSunlight (Block b) {
        return isDayTime(b) && b.getWorld().isClearWeather();
    }

    public int getSunlight (Block b) {
        if (hasSunlight(b)) {
            return b.getLightFromSky();
        } else {
            return 0;
        }
    }

    public double getSunHeightInSky (Block b) {
        long adjustedTime = b.getWorld().getTime() + 1000;

        if (0 < adjustedTime && adjustedTime > LibBlocks.getInstance().getWeatherConstants().NIGHT_START_TICKS + 1000) {
            if (adjustedTime <= LibBlocks.getInstance().getWeatherConstants().NOON_TICKS + 1000) {
                return 2 * adjustedTime / LibBlocks.getInstance().getWeatherConstants().DAY_LENGTH_TICKS;
            } else {
                return 2 * (1 - adjustedTime / LibBlocks.getInstance().getWeatherConstants().DAY_LENGTH_TICKS);
            }
        } else {
            return 0;
        }
    }
}
