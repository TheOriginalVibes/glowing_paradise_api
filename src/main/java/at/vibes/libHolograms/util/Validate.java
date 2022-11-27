package at.vibes.libHolograms.util;

public class Validate {
    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

}
