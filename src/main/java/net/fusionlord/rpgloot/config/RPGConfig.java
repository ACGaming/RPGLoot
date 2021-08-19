package net.fusionlord.rpgloot.config;

import java.util.Map;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.fusionlord.rpgloot.RPGLoot;

@Config(modid = RPGLoot.MODID, name = "RPGLoot")
@EventBusSubscriber(modid = RPGLoot.MODID)
public class RPGConfig
{
    @Config.Comment("General")
    public static GeneralCategory general = new GeneralCategory();

    @Name("Mobs list")
    @Comment({"List of mobs to toggle on and off."})
    public static Map<String, Boolean> mobsList = Maps.newHashMap();

    @Name("Particles")
    @Comment({"Change corpse particle settings."})
    public static ParticleCategory particles = new ParticleCategory();

    @Name("Debug")
    @Comment({"Change debug settings."})
    public static DebugCategory debug = new DebugCategory();

    public static void initializeMobList()
    {
        for (ResourceLocation rl : EntityList.getEntityNameList())
        {
            Class<?> c = EntityList.getClass(rl);
            if (c != null && EntityLivingBase.class.isAssignableFrom(c))
            {
                mobsList.put(rl.toString(), mobsList.getOrDefault(rl.toString(), Boolean.FALSE));
                RPGLoot.logger.info("Registering: " + rl);
            }
        }
        ConfigManager.sync(RPGLoot.MODID, Config.Type.INSTANCE);
    }

    public static boolean isBlackListed(Entity entity)
    {
        ResourceLocation rl = EntityList.getKey(entity);
        return (rl == null || isBlackListed(rl));
    }

    public static boolean isBlackListed(ResourceLocation resourceLocation)
    {
        return (mobsList != null && mobsList.containsKey(resourceLocation.toString()) && mobsList.get(resourceLocation.toString()) && general.isBlacklist);
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(RPGLoot.MODID))
        {
            RPGLoot.logger.info("Config changed");
            ConfigManager.sync(RPGLoot.MODID, Config.Type.INSTANCE);
        }
    }

    public static class GeneralCategory
    {
        @Name("Corpse life time")
        @RangeInt(min = -1)
        @Comment({"Time in minutes before corpses decay. Set to -1 for no decay."})
        public int corpseDecayTime = 10;

        @Name("Collect drops")
        @Comment({"Drops are collected in the corpse."})
        public boolean collectDrops = true;

        @Name("Mob list is blacklist")
        @Comment({"Use mob list as a blacklist."})
        public boolean isBlacklist = true;

        @Name("Player corpses")
        @Comment({"Players spawn corpses upon death."})
        public boolean doPlayers = true;

        @Name("Empty corpses")
        @Comment({"Corpses with no loot are allowed to spawn."})
        public boolean emptyCorpses = false;

        @Name("Loot stealing")
        @Comment({"Players can steal foreign player's loot from corpses."})
        public boolean playerStealing = false;

        @Name("Player kills only")
        @Comment({"Only drop corpses on player kills."})
        public boolean playerKillsOnly = true;
    }

    public static class ParticleCategory
    {
        @Name("Show loot particle")
        @Comment({"Show particles if a corpse contains items."})
        public boolean spawnItem = true;

        @Name("Loot particle")
        @Comment({"Particle if a corpse contains items."})
        public EnumParticleTypes itemParticle = EnumParticleTypes.VILLAGER_HAPPY;

        @Name("Show empty particle")
        @Comment({"Show particles if a corpse doesn't contain items."})
        public boolean spawnEmpty;

        @Name("Empty particle")
        @Comment({"Particle if a corpse doesn't contain items."})
        public EnumParticleTypes emptyParticle = EnumParticleTypes.SMOKE_NORMAL;

        @Name("Chance")
        @Comment({"Particle chance per tick."})
        @RangeInt(min = 0, max = 100)
        public int chance = 15;
    }

    public static class DebugCategory
    {
        @Name("Logging")
        @Comment({"Debug messages will be printed in the log file."})
        public boolean enableDebugLogging = false;
    }
}