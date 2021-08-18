package net.fusionlord.rpgloot.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.fusionlord.rpgloot.CommonProxy;
import net.fusionlord.rpgloot.client.rendering.CorpseRenderFactory;
import net.fusionlord.rpgloot.entities.EntityCorpse;

public class ClientProxy extends CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityCorpse.class, new CorpseRenderFactory());
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    public World getWorld()
    {
        return (Minecraft.getMinecraft()).world;
    }

    public void playSound(String sound) {}
}