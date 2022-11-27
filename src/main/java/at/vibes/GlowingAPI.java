package at.vibes;

import at.vibes.libBlocks.LibBlocks;
import at.vibes.libHolograms.LibHolograms;
import at.vibes.libInventorys.LibInv;
import at.vibes.libMinecraft.LibMinecraft;
import at.vibes.libNPC.LibNPC;
import at.vibes.libSQL.LibMySQL;

public abstract class GlowingAPI {
    private static GlowingAPI instance;

    public abstract LibMinecraft getMinecraftLib();
    public abstract LibBlocks getBlocksLib();
    public abstract LibInv getInventoryLib();
    public abstract LibNPC getNPCLib();
    public abstract LibHolograms getHologramLib();
    public abstract LibMySQL getMySQLLib();


    public GlowingAPI() {
        instance = this;
    }

    public static GlowingAPI getInstance() {return instance;}
}
