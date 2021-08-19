package net.fusionlord.rpgloot.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.fusionlord.rpgloot.inventory.LootContainer;
import net.fusionlord.rpgloot.packets.DisposePacket;

public class LootGUI extends GuiContainer
{
    private final EntityCorpse corpse;
    private final EntityPlayer player;
    private GuiButton dispose;

    public LootGUI(LootContainer lootContainer, EntityCorpse entCorpse, EntityPlayer entPlayer)
    {
        super(lootContainer);
        this.corpse = entCorpse;
        this.player = entPlayer;
    }

    public void initGui()
    {
        super.initGui();
        this.dispose = new GuiButton(0, this.guiLeft, this.guiTop - 25, this.xSize, 20, "Loot & Dispose");
        addButton(this.dispose);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        this.mc.renderEngine.bindTexture(new ResourceLocation(RPGLoot.MODID, "textures/loottable.png"));
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 177);
        for (Slot s : this.inventorySlots.inventorySlots)
            drawTexturedModalRect(this.guiLeft + s.xPos - 1, this.guiTop + s.yPos - 1, 176, 0, 20, 20);
    }

    public void actionPerformed(GuiButton button)
    {
        if (button == this.dispose)
        {
            RPGLoot.INSTANCE.getPacketHandler().sendToServer(new DisposePacket(this.corpse));
            this.player.closeScreen();
        }
    }
}