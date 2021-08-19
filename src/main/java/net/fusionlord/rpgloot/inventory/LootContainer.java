package net.fusionlord.rpgloot.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.fusionlord.rpgloot.config.RPGConfig;
import net.fusionlord.rpgloot.entities.EntityCorpse;

public class LootContainer extends Container
{
    protected EntityCorpse corpse;

    public LootContainer(EntityCorpse entCorpse, InventoryPlayer player_inventory)
    {
        this.corpse = entCorpse;
        for (int i = 0; i < this.corpse.getSizeInventory(); i++)
        {
            addSlotToContainer(new Slot(this.corpse, i, 7 + i % 9 * 18, 7 - i / 9 * 18));
        }
        bindPlayerInventory(player_inventory);
    }

    public EntityCorpse getCorpse()
    {
        return this.corpse;
    }

    public ItemStack transferStackInSlot(EntityPlayer p, int i)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < this.corpse.getSizeInventory())
            {
                if (!mergeItemStack(itemstack1, this.corpse.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, this.corpse.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public boolean canInteractWith(EntityPlayer player)
    {
        if (RPGConfig.general.playerStealing)
        {
            return true;
        }
        else
        {
            return this.corpse.isUsableByPlayer(player);
        }
    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        int y = (this.corpse.getSizeInventory() > 0) ? ((this.corpse.getSizeInventory() / 9 + 1) * 18 + 12) : 10;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++)
        {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++)
            {
                addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, 7 + playerInvCol * 18, y + (4 - playerInvRow) * 18 - 39));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
        {
            addSlotToContainer(new Slot(playerInventory, hotbarSlot, 7 + hotbarSlot * 18, y + 54));
        }
    }
}