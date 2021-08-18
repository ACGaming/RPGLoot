package net.fusionlord.rpgloot.packets;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.fusionlord.rpgloot.entities.EntityCorpse;

public class ReqCorpseSyncPacket extends CorpsePacket
{
    public ReqCorpseSyncPacket() {}

    public ReqCorpseSyncPacket(EntityCorpse corpse)
    {
        super(corpse);
    }

    public static class HANDLER implements IMessageHandler<ReqCorpseSyncPacket, IMessage>
    {
        public IMessage onMessage(ReqCorpseSyncPacket message, MessageContext ctx)
        {
            World world = (ctx.getServerHandler()).player.world;
            Entity entity = world.getEntityByID(message.corpseID);
            if (entity instanceof EntityCorpse)
                ((EntityCorpse) entity).markDirty();
            return null;
        }
    }
}