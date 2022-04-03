package net.fusionlord.rpgloot.client.events;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import net.fusionlord.rpgloot.RPGLoot;

@ObjectHolder(RPGLoot.MODID)
public class RPGSoundEvents
{
    public static final SoundEvent LOOTING_REPORT = new SoundEvent(new ResourceLocation(RPGLoot.MODID + ":looting.report"));
}