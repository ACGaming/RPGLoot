package net.fusionlord.rpgloot.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import net.fusionlord.rpgloot.client.gui.LootGUI;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.fusionlord.rpgloot.inventory.LootContainer;

public class GUIHandler implements IGuiHandler
{
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        LootContainer lootContainer = null;
        EntityCorpse corpse;
        if (ID == 0)
        {
            corpse = (EntityCorpse) world.getEntityByID(x);
            lootContainer = new LootContainer(corpse, player.inventory);
        }
        return lootContainer;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 0)
        {
            return new LootGUI((LootContainer) getServerGuiElement(ID, player, world, x, y, z), (EntityCorpse) world.getEntityByID(x), player);
        }
        return null;
    }
}