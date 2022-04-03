package net.fusionlord.rpgloot.handlers;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.config.RPGConfig;
import net.fusionlord.rpgloot.entities.EntityCorpse;

@EventBusSubscriber(modid = RPGLoot.MODID)
public class CommonEventHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDrops(LivingDropsEvent event)
    {
        if (EntityList.getKey(event.getEntityLiving()).getResourceDomain().equals("animania") || EntityList.getKey(event.getEntityLiving()).getResourceDomain().equals("customnpcs") || (event.getEntityLiving() instanceof EntityPlayer && !RPGConfig.general.doPlayers))
        {
            return;
        }
        if (!(event.getEntity()).world.isRemote && ((RPGConfig.general.isBlacklist && !RPGConfig.isBlackListed(event.getEntityLiving())) || (!RPGConfig.general.isBlacklist && RPGConfig.isBlackListed(event.getEntityLiving()))))
        {
            if (RPGConfig.debug.enableDebugLogging)
            {
                RPGLoot.logger.info("Event drops: " + event.getDrops());
            }
            if (!RPGConfig.general.emptyCorpses && event.getDrops().isEmpty())
            {
                return;
            }
            if (!RPGConfig.general.playerKillsOnly || event.getSource().getTrueSource() instanceof EntityPlayer)
            {
                (event.getEntity()).world.spawnEntity(new EntityCorpse((event.getEntity()).world, event.getEntityLiving(), (event.getSource().getTrueSource() instanceof EntityPlayer) ? (EntityPlayer) event.getSource().getTrueSource() : null, event.getDrops()));
            }
            if (RPGConfig.general.collectDrops)
            {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
    {
        EntityEntry entry = new EntityEntry(EntityCorpse.class, "corpse");
        entry.setRegistryName(new ResourceLocation(RPGLoot.MODID, "corpse"));
        event.getRegistry().register(entry);
    }
}