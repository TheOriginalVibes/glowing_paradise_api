package at.vibes.libHolograms.placeholder;

import at.vibes.libHolograms.util.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Placeholders {
    private final Map<String, Function<Player, String>> placeholders = new ConcurrentHashMap<>();

    public void add(@NotNull String key, @NotNull Function<Player, String> result) {
        Validate.notNull(key, "Key cannot be null");
        Validate.notNull(result, "Function cannot be null");
        placeholders.put(key, result);
    }

    @NotNull
    public String parse(@NotNull String line, @NotNull Player player) {
        String c = line;
        for (Map.Entry<String, Function<Player, String>> entry : placeholders.entrySet()) {
            c = c.replaceAll(entry.getKey(), entry.getValue().apply(player));
        }
        return c;
    }
}
