package at.vibes.libMinecraft.utils.replacer;

import at.vibes.libMinecraft.utils.replacer.internal.RGBLine;
import com.google.common.collect.ImmutableList;

import java.util.function.Function;

public interface StringReplacer extends Function<String, String> {
    ImmutableList<StringReplacer> DEFAULT_REPLACES = ImmutableList.of(new RGBLine());

    static String of(String string) {
        return DEFAULT_REPLACES.stream()
                .filter(StringReplacer::isSupported)
                .map(p -> (Function<String, String>) p)
                .reduce(Function.identity(), Function::andThen)
                .apply(string);
    }

    String apply(String string);
    boolean isSupported();
}
