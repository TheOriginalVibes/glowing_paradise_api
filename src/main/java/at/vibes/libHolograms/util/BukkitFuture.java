package at.vibes.libHolograms.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BukkitFuture {

    public static <T> CompletableFuture<T> supplyAsync(@NotNull Plugin plugin,
                                                       @NotNull Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public static CompletableFuture<Void> runAsync(@NotNull Plugin plugin,
                                                   @NotNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public static <T> CompletableFuture<T> supplySync(@NotNull Plugin plugin,
                                                      @NotNull Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (Bukkit.isPrimaryThread()) {
            try {
                future.complete(supplier.get());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    future.complete(supplier.get());
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        }
        return future;
    }

    public static CompletableFuture<Void> runSync(@NotNull Plugin plugin,
                                                  @NotNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (Bukkit.isPrimaryThread()) {
            try {
                runnable.run();
                future.complete(null);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    runnable.run();
                    future.complete(null);
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        }
        return future;
    }

    public static <T> BiConsumer<T, ? super Throwable> sync(@NotNull Plugin plugin,
                                                            @NotNull BiConsumer<T, ? super Throwable> action) {
        return (BiConsumer<T, Throwable>) (t, throwable) -> runSync(plugin,
                () -> action.accept(t, throwable));
    }
}