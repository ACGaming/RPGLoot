package net.fusionlord.rpgloot.client.gui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Loader;

import net.fusionlord.rpgloot.RPGLoot;

public class GUIMobsScreen extends GuiConfig
{
    protected GuiButtonExt nextMod;
    protected GuiButtonExt prevMod;
    protected GuiButtonExt apply;
    protected Map<String, List<IConfigElement>> modElements = Maps.newHashMap();
    protected int index = 0;

    public GUIMobsScreen(GuiConfig guiConfig)
    {
        super(guiConfig.parentScreen, guiConfig.modID, true, false, guiConfig.title);
        for (IConfigElement element : guiConfig.configElements)
        {
            String modid = element.getName().split(":")[0];
            List<IConfigElement> list = this.modElements.getOrDefault(modid, Lists.newArrayList());
            list.add(element);
            this.modElements.put(modid, list);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(this.nextMod = new GuiButtonExt(100000, this.width - 60, 5, 55, 20, "Next Mod"));
        this.buttonList.add(this.prevMod = new GuiButtonExt(100001, this.width - 120, 5, 55, 20, "Prev Mod"));
        this.buttonList.add(this.apply = new GuiButtonExt(100002, 5, 5, 55, 20, "Apply"));
        updateEntries();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button == this.apply || (button == this.nextMod && next()) || (button == this.prevMod && prev()))
        {
            updateEntries();
        }
        else
        {
            super.actionPerformed(button);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        if (this.entryList == null || this.needsRefresh)
        {
            this.entryList = new GuiConfigEntries(this, this.mc);
            this.needsRefresh = false;
        }
    }

    private boolean prev()
    {
        return ((this.index = clamp(this.index - 1)) != -1);
    }

    private boolean next()
    {
        return ((this.index = clamp(this.index + 1)) != -1);
    }

    private int clamp(int index)
    {
        return MathHelper.clamp(index, 0, this.modElements.size() - 1);
    }

    private void updateEntries()
    {
        String modid = (String) this.modElements.keySet().toArray()[this.index];
        this.entryList.saveConfigElements();
        this.configElements.clear();
        this.configElements.addAll(this.modElements.get(modid));
        this.titleLine2 = Loader.instance().getModList().stream().filter(c -> c.getModId().equals(modid)).collect(Collectors.toList()).get(0).getName();
        this.needsRefresh = true;
        ConfigManager.sync(RPGLoot.MODID, Config.Type.INSTANCE);
        RPGLoot.logger.info("Config synced");
    }
}