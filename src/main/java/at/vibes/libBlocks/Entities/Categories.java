package at.vibes.libBlocks.Entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class Categories {

    public boolean isTNT (Entity e) {
        return switch (e.getType()) {
            case PRIMED_TNT -> true;
            case MINECART_TNT -> true;
            default -> false;
        };
    }

    public boolean isAZombie (LivingEntity le) {
        return switch (le.getType()) {
            case ZOMBIE -> true;
            case ZOMBIFIED_PIGLIN -> true;
            case ZOMBIE_VILLAGER -> true;
            case HUSK -> true;
            case DROWNED -> true;
            default -> false;
        };
    }
}
