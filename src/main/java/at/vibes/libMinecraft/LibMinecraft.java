package at.vibes.libMinecraft;

import at.vibes.libMinecraft.bitmask.BitMask;
import at.vibes.libMinecraft.bitmask.ByteBitMask;
import at.vibes.libMinecraft.bitmask.LongBitMask;
import at.vibes.libMinecraft.bitmask.ShortBitMask;
import at.vibes.libMinecraft.command.BaseCommand;
import at.vibes.libMinecraft.command.CommandArgs;
import at.vibes.libMinecraft.command.CommandException;
import at.vibes.libMinecraft.command.SubCommand;
import at.vibes.libMinecraft.math.*;
import at.vibes.libMinecraft.translation.TranslatablePlugin;
import at.vibes.libMinecraft.translation.Translation;
import at.vibes.libMinecraft.utils.ChatColor;
import at.vibes.libMinecraft.utils.Convert;
import at.vibes.libMinecraft.utils.StringUtils;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.lang.reflect.Method;

public abstract class LibMinecraft {
    private static LibMinecraft instance;

    LibMinecraft() {instance = this;}

    //BitMasks
    public abstract BitMask getBitMask();
    public abstract BitMask getBitMask(int mask);
    public abstract ByteBitMask getByteBitMask();
    public abstract ByteBitMask getByteBitMask(byte mask);
    public abstract LongBitMask getLongBitMask();
    public abstract LongBitMask getLongBitMask(long mask);
    public abstract ShortBitMask getShortBitMask();
    public abstract ShortBitMask getShortBitMask(short mask);

    //Command
    public abstract BaseCommand getBaseCommand();
    public abstract BaseCommand getBaseCommand(TranslatablePlugin plugin, String permissionBase);
    public abstract BaseCommand getBaseCommand(TranslatablePlugin plugin, String permissionBase, PermissionDefault parentDefault);

    public abstract CommandException getCommandException();

    public abstract SubCommand getSubCommand();
    public abstract SubCommand getSubCommand(Object commandContainer, Method method, String name, String[] aliases, Permission permission, boolean addPermissionParent, String usage);

    public abstract CommandArgs getCommandArgs();
    public abstract CommandArgs getCommandArgs(BaseCommand baseCommand, String baseLabel, SubCommand subCommand, String[] args);

    //Math
    public abstract Cube getCube();
    public abstract Cube getCube(Vector3 corner, int size);
    public abstract Cube getCube(Vector3 corner, double size);

    public abstract Cuboid getCuboid();
    public abstract Cuboid getCuboid(Vector3 corner1, Vector3 corner2);

    public abstract Rectangle getRectangle();
    public abstract Rectangle getRectangle(Vector2 corner1, Vector2 corner2);

    public abstract Square getSquare();
    public abstract Square getSquare(Vector2 corner, int size);
    public abstract Square getSquare(Vector2 corner, double size);

    public abstract Vector2 getVector2();            // A 2d Vector
    public abstract Vector2 getVector2(int x, int y);
    public abstract Vector2 getVector2(double x, double y);

    public abstract Vector3 getVector3();            // a 3d Vector
    public abstract Vector3 getVector3(final int x, final int y, final int z);
    public abstract Vector3 getVector3(final double x, final double y, final double z);

    //Translation
    public abstract Translation getTranslation();
    public abstract Translation getTranslation(Class clazz, final String language);

    public abstract TranslatablePlugin getTranslatablePlugin();

    //ChatColor
    public abstract ChatColor getChatColor();

    //Convert
    public abstract Convert getConvert();

    //StringUtils
    public abstract StringUtils getStringUtils();

    public static LibMinecraft getInstance() {return instance;}
}
