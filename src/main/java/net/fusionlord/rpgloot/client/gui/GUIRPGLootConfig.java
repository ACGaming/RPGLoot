package net.fusionlord.rpgloot.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.config.RPGConfig;

public class GUIRPGLootConfig extends GuiConfig
{
    GUIRPGLootConfig(GuiScreen gui)
    {
        super(gui, RPGLoot.MODID, false, false, "RPGLoot Config", RPGConfig.class);
    }
}