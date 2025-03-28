package net.fusionlord.rpgloot.packets;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.fusionlord.rpgloot.entities.EntityCorpse;

public class DisposePacket extends CorpsePacket
{
    public DisposePacket() {}

    public DisposePacket(EntityCorpse corpse)
    {
        super(corpse);
    }

    public static class HANDLER implements IMessageHandler<DisposePacket, IMessage>
    {
        public IMessage onMessage(DisposePacket message, MessageContext ctx)
        {
            EntityCorpse corpse = (EntityCorpse) (ctx.getServerHandler()).player.world.getEntityByID(message.corpseID);
            if (corpse != null && corpse.lootToPlayer((ctx.getServerHandler()).player))
            {
                corpse.dispose();
            }
            return null;
        }
    }
}