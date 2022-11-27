package at.vibes.libBlocks.Players;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModes {

    public boolean isInSurvivalMode (Player p) {
        return p.getGameMode().equals(GameMode.SURVIVAL);
    }

    public boolean isInCreativeMode (Player p) {
        return p.getGameMode().equals(GameMode.CREATIVE);
    }

    public boolean isInAdventureMode (Player p) {
        return p.getGameMode().equals(GameMode.ADVENTURE);
    }

    public boolean isInSpectatorMode (Player p) {
        return p.getGameMode().equals(GameMode.SPECTATOR);
    }
}
