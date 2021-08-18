package net.fusionlord.rpgloot;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import net.fusionlord.rpgloot.config.RPGConfig;
import net.fusionlord.rpgloot.handlers.PacketHandler;
import net.fusionlord.rpgloot.packets.CorpseSyncPacket;
import net.fusionlord.rpgloot.packets.DisposePacket;
import net.fusionlord.rpgloot.packets.LootPacket;
import net.fusionlord.rpgloot.packets.ReqCorpseSyncPacket;

@Mod(modid = "rpgloot", name = "RPGLoot", version = "1.11", guiFactory = "net.fusionlord.rpgloot.client.gui.GUIFactory", acceptedMinecraftVersions = "[1.12.2]")
public class RPGLoot
{
    public static final String MODID = "rpgloot";
    public static final String MODNAME = "RPGLoot";
    public static final String VERSION = "1.11";

    public static final Logger logger = new Logger();

    @Instance("rpgloot")
    public static RPGLoot INSTANCE;

    @SidedProxy(clientSide = "net.fusionlord.rpgloot.client.ClientProxy", serverSide = "net.fusionlord.rpgloot.CommonProxy")
    public static CommonProxy proxy;

    private PacketHandler packetHandler;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        setUpPacketHandler();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        RPGConfig.initializeMobList();
        proxy.postInit(event);
    }

    public PacketHandler getPacketHandler()
    {
        return this.packetHandler;
    }

    private void setUpPacketHandler()
    {
        this.packetHandler = new PacketHandler("RPGLoot");
        this.packetHandler.registerMessage(LootPacket.HANDLER.class, LootPacket.class, Side.SERVER);
        this.packetHandler.registerMessage(DisposePacket.HANDLER.class, DisposePacket.class, Side.SERVER);
        this.packetHandler.registerMessage(ReqCorpseSyncPacket.HANDLER.class, ReqCorpseSyncPacket.class, Side.SERVER);
        this.packetHandler.registerMessage(CorpseSyncPacket.HANDLER.class, CorpseSyncPacket.class, Side.CLIENT);
    }
}