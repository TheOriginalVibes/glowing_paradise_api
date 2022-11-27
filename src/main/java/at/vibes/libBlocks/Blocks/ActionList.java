package at.vibes.libBlocks.Blocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public final class ActionList {
    private final List<Block> la, lb;

    private final Comparator<? super Block> s;
    private final Consumer<? super Block> a;

    private int taskID;
    private final Plugin p;
    private final long t;

    private boolean toggle;

    private void process () {
        if (toggle) {
            lb.stream().sorted(s).forEachOrdered(a);

            lb.clear();
        } else {
            la.stream().sorted(s).forEachOrdered(a);

            la.clear();
        }

        toggle = !toggle;
    }

    public final void start (long d) {
        if (!Bukkit.getServer().getScheduler().isCurrentlyRunning(taskID)) {
            taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, this::process, d, t);
        } else {
            stop();
            start(d);
        }
    }

    public final void stop () {
        Bukkit.getServer().getScheduler().cancelTask(taskID);

        taskID = -1;
    }

    public final void addBlock (Block b) {
        if (toggle) {
            if (!la.contains(b)) {
                la.add(b);
            }
        } else {
            if (!lb.contains(b)) {
                lb.add(b);
            }
        }
    }

    public void removeBlock (Block b) {
        if (toggle) {
            la.remove(b);
        } else {
            lb.remove(b);
        }
    }

    public List<Block> getBlocks () {
        if (toggle) {
            return la;
        } else {
            return lb;
        }
    }

    public ActionList(
            Comparator<? super Block> s,
            Consumer<? super Block> a,
            long t,
            long d,
            Plugin p
    ) {
        la = new ArrayList<>();
        lb = new ArrayList<>();

        this.s = s;
        this.a = a;
        this.t = t;
        this.p = p;

        toggle = false;

        start(d);
    }
}
