package net.fusionlord.rpgloot.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import net.fusionlord.rpgloot.CommonProxy;
import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.client.ClientProxy;
import net.fusionlord.rpgloot.config.RPGConfig;
import net.fusionlord.rpgloot.packets.CorpseSyncPacket;
import net.fusionlord.rpgloot.packets.ReqCorpseSyncPacket;

public class EntityCorpse extends Entity implements IInventory
{
    private List<ItemStack> drops;
    private UUID owner;
    private NBTTagCompound oldEntityData;
    private boolean dispose;

    public EntityCorpse(World worldIn)
    {
        super(worldIn);
        this.oldEntityData = new NBTTagCompound();
        setEntityClass("");
        this.drops = new ArrayList<>();
        setSize(1.0F, 1.0F);
    }

    public EntityCorpse(World worldIn, EntityLivingBase entityLivingBase, EntityPlayer player, List<EntityItem> entityDrops)
    {
        this(worldIn);
        copyData(entityLivingBase);
        if (RPGConfig.general.collectDrops)
        {
            addDrops(entityDrops);
        }
        setLocationAndAngles(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, 0.0F, 0.0F);
        if (player != null)
        {
            this.owner = player.getPersistentID();
        }
    }

    public int getSizeInventory()
    {
        return (this.drops != null) ? this.drops.size() : 0;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public ItemStack getStackInSlot(int index)
    {
        if (index >= this.drops.size())
        {
            return ItemStack.EMPTY;
        }
        return this.drops.get(index);
    }

    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack currentStack = getStackInSlot(index);
        if (currentStack != null)
        {
            if (currentStack.getCount() <= count)
            {
                this.drops.remove(currentStack);
                return currentStack;
            }
            ItemStack itemstack = currentStack.splitStack(count);
            if (currentStack.getCount() == 0)
            {
                this.drops.remove(currentStack);
            }
            return itemstack;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStack.EMPTY;
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index < this.drops.size())
        {
            this.drops.set(index, stack);
        }
        else
        {
            this.drops.add(stack);
        }
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void markDirty()
    {
        for (int i = 0; i < this.drops.size(); i++)
        {
            if (this.drops.get(i) == ItemStack.EMPTY)
            {
                this.drops.remove(i);
            }
        }
        RPGLoot.INSTANCE.getPacketHandler().sendToAllAround(new CorpseSyncPacket(this), new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 64.0D));
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return (this.owner == null || this.owner == player.getPersistentID());
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value) {}

    public int getFieldCount()
    {
        return 0;
    }

    public void clear() {}

    public ItemStack getStackInSlotOnClosing(int index)
    {
        return getStackInSlot(index);
    }

    public NBTTagCompound getOldEntityData()
    {
        return this.oldEntityData;
    }

    public String getEntityClass()
    {
        return this.oldEntityData.getString("entityClass");
    }

    public void setEntityClass(String entClass)
    {
        this.oldEntityData.setString("entityClass", entClass);
    }

    public boolean lootToPlayer(EntityPlayer player)
    {
        if (this.drops != null && player != null)
        {
            Iterator<ItemStack> iterator = this.drops.iterator();
            while (iterator.hasNext())
            {
                ItemStack itemStack = iterator.next();
                if (!player.inventory.addItemStackToInventory(itemStack))
                {
                    player.dropItem(itemStack, false);
                }
                iterator.remove();
            }
            return true;
        }
        return false;
    }

    public void dispose()
    {
        this.dispose = true;
    }

    protected void entityInit() {}

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if ((getEntityClass() == null || getEntityClass().isEmpty()) && this.world.isRemote)
        {
            RPGLoot.INSTANCE.getPacketHandler().sendToServer(new ReqCorpseSyncPacket(this));
        }
        if (getPosition().getY() > 1)
        {
            move(MoverType.SELF, 0.0D, -0.15D, 0.0D);
        }
        if (this.world.isRemote && this.rand.nextFloat() <= RPGConfig.particles.chance / 100.0F)
        {
            float x = (float) this.posX - 0.5F;
            float y = (float) this.posY + 0.2F;
            float z = (float) this.posZ - 0.5F;
            float var1 = this.rand.nextFloat() * 0.5F * 2.0F - this.rand.nextInt(1);
            float var2 = this.rand.nextFloat() * 0.5F * 2.0F - this.rand.nextInt(1);
            float var3 = this.rand.nextFloat() * 0.5F * 3.0F - this.rand.nextInt(2);

            if (RPGConfig.particles.spawnEmpty && this.drops.isEmpty())
            {
                this.world.spawnParticle(RPGConfig.particles.emptyParticle, x + var1, y + var3, z + var2, 0.5D, 0.5D, 0.5D);
            }
            else if (RPGConfig.particles.spawnItem)
            {
                this.world.spawnParticle(RPGConfig.particles.itemParticle, x + var1, y + var3, z + var2, 0.5D, 0.5D, 0.5D);
            }
            return;
        }
        if (this.dispose || (RPGConfig.general.corpseDecayTime > -1 && this.ticksExisted / 20 / 60 > RPGConfig.general.corpseDecayTime) || (!RPGConfig.general.emptyCorpses && this.drops.isEmpty() && this.ticksExisted > 20))
        {
            setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound.getBoolean("hasOwner"))
        {
            this.owner = new UUID(tagCompound.getLong("UUID1"), tagCompound.getLong("UUID2"));
        }
        else
        {
            this.owner = null;
        }
        this.oldEntityData = tagCompound.getCompoundTag("entityData");
        if (tagCompound.hasKey("dropCount"))
        {
            int count = tagCompound.getInteger("dropCount");
            this.drops = new ArrayList<>();
            NBTTagCompound dropsTag = tagCompound.getCompoundTag("drops");
            for (int i = 0; i < count; i++)
            {
                this.drops.add(new ItemStack(dropsTag.getCompoundTag("drop:".concat(String.valueOf(i)))));
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setTag("entityData", this.oldEntityData);
        tagCompound.setBoolean("hasOwner", (this.owner != null));
        if (this.owner != null)
        {
            tagCompound.setLong("UUID1", this.owner.getMostSignificantBits());
            tagCompound.setLong("UUID2", this.owner.getLeastSignificantBits());
        }
        if (this.drops != null)
        {
            tagCompound.setInteger("dropCount", this.drops.size());
            NBTTagCompound dropsTag = new NBTTagCompound();
            for (int i = 0; i < this.drops.size(); i++)
            {
                if (this.drops.get(i) != null)
                {
                    NBTTagCompound dropTag = new NBTTagCompound();
                    this.drops.get(i).writeToNBT(dropTag);
                    dropsTag.setTag("drop:".concat(String.valueOf(i)), dropTag);
                }
            }
            tagCompound.setTag("drops", dropsTag);
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if ((isUsableByPlayer(player) || RPGConfig.general.playerStealing) && !this.world.isRemote)
        {
            player.openGui(RPGLoot.INSTANCE, 0, this.world, getEntityId(), 0, 0);
        }
        else if (!isUsableByPlayer(player) && this.world.isRemote)
        {
            CommonProxy proxy = RPGLoot.proxy;
            if (proxy instanceof ClientProxy)
            {
                ((ClientProxy) proxy).playSound("looting.stealing");
            }
        }
        return true;
    }

    @Override
    public boolean canRenderOnFire()
    {
        return false;
    }

    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation("entity.corpse.name", this.oldEntityData.getString("CustomName"));
    }

    private void addDrops(List<EntityItem> itemEntities)
    {
        for (EntityItem entityItem : itemEntities)
        {
            if (entityItem == null)
            {
                continue;
            }
            addDrop(entityItem.getItem());
        }
    }

    private void copyData(EntityLivingBase entityLivingBase)
    {
        this.oldEntityData = new NBTTagCompound();
        setEntityClass(entityLivingBase.getClass().getCanonicalName());
        entityLivingBase.writeToNBT(this.oldEntityData);
        this.oldEntityData.setBoolean("NoAI", true);
        this.oldEntityData.removeTag("Fire");
        this.oldEntityData.setShort("HurtTime", (short) 0);
    }

    private void addDrop(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return;
        }
        for (ItemStack currentStack : this.drops)
        {
            if (ItemStack.areItemStacksEqual(currentStack, itemStack) && currentStack.getCount() < getInventoryStackLimit())
            {
                int remainder = getInventoryStackLimit() - currentStack.getCount();
                if (itemStack.getCount() >= remainder)
                {
                    currentStack.setCount(getInventoryStackLimit());
                    itemStack.setCount(itemStack.getCount() - remainder);
                    continue;
                }
                currentStack.setCount(currentStack.getCount() + itemStack.getCount());
                return;
            }
        }
        this.drops.add(itemStack);
    }
}