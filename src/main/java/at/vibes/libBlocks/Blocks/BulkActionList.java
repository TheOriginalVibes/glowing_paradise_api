package at.vibes.libBlocks.Blocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public abstract class BulkActionList {
    private final List<Block> la, lb;

    private int taskID;
    private final Plugin p;
    private final long t;

    private boolean toggle;

    private void process() {
        if (toggle) {
            action(lb);

            lb.clear();
        } else {
            action(la);

            la.clear();
        }

        toggle = !toggle;
    }

    protected abstract void action(List<Block> l);

    public final void start(long d) {
        if (!Bukkit.getServer().getScheduler().isCurrentlyRunning(taskID)) {
            taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p, this::process, d, t);
        } else {
            stop();
            start(d);
        }
    }

    public final void stop() {
        Bukkit.getServer().getScheduler().cancelTask(taskID);

        taskID = -1;
    }

    public final void addBlock(Block b) {
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

    public final void removeBlock(Block b) {
        if (toggle) {
            la.remove(b);
        } else {
            lb.remove(b);
        }
    }

    public final List<Block> getBlocks() {
        if (toggle) {
            return la;
        } else {
            return lb;
        }
    }

    public BulkActionList(
            long t,
            long d,
            Plugin p
    ) {
        la = new ArrayList<>();
        lb = new ArrayList<>();

        this.t = t;
        this.p = p;

        this.toggle = false;

        start(d);
    }
}
