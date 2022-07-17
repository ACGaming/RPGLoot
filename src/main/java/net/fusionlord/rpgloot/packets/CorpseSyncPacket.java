package net.fusionlord.rpgloot.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;
import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.entities.EntityCorpse;

public class CorpseSyncPacket extends CorpsePacket
{
    public NBTTagCompound corpseTag;

    public CorpseSyncPacket() {}

    public CorpseSyncPacket(EntityCorpse corpse)
    {
        super(corpse);
        this.corpseTag = new NBTTagCompound();
        corpse.writeToNBT(this.corpseTag);
    }

    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        this.corpseTag = ByteBufUtils.readTag(buf);
    }

    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        ByteBufUtils.writeTag(buf, this.corpseTag);
    }

    public static class HANDLER implements IMessageHandler<CorpseSyncPacket, IMessage>
    {
        public IMessage onMessage(CorpseSyncPacket message, MessageContext ctx)
        {
            //World world = RPGLoot.proxy.getWorld();
            EntityPlayerMP player = ctx.getServerHandler().player;
            World world = player.getEntityWorld();
            Entity entity = world.getEntityByID(message.corpseID);
            if (entity instanceof EntityCorpse)
            {
                entity.readFromNBT(message.corpseTag);
            }
            return null;
        }
    }
}