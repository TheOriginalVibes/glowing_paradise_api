package at.vibes.libHolograms.animation;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Animation {

    long delay();

    void nextFrame(@NotNull Player player, int entityID, Location location);

    boolean async();

    Animation newInstance();

    enum AnimationType {
        CIRCLE(new CircleAnimation());

        public final Animation type;

        AnimationType(Animation type) {
            this.type = type;
        }

    }

}