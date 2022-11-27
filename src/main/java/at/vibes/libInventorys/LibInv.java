package at.vibes.libInventorys;

import at.vibes.libInventorys.manager.InventoryManager;

public abstract class LibInv {
    private static LibInv instance;

    LibInv() {instance = this;}

    public abstract InventoryManager getInventoryManager();

    public static LibInv getInstance() {return instance;}
}
