package at.vibes.libHolograms.animation;

import at.vibes.libHolograms.packet.PacketsFactory;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CircleAnimation implements Animation {

    private float yaw = 0;

    @Override
    public long delay() {
        return 3L;
    }

    @Override
    public void nextFrame(@NotNull Player player, int entityID, Location location) {
        this.yaw += 10L;

        PacketsFactory.get()
                .rotatePackets(entityID, location, yaw)
                .forEach(p -> p.send(player));
    }

    @Override
    public boolean async() {
        return true;
    }

    @Override
    public Animation newInstance() {
        return new CircleAnimation();
    }

}