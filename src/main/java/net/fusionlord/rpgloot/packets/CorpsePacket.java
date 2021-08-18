package net.fusionlord.rpgloot.packets;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import io.netty.buffer.ByteBuf;
import net.fusionlord.rpgloot.entities.EntityCorpse;

public class CorpsePacket implements IMessage
{
    public int corpseID;

    public CorpsePacket() {}

    public CorpsePacket(EntityCorpse corpse)
    {
        this.corpseID = corpse.getEntityId();
    }

    public void fromBytes(ByteBuf buf)
    {
        this.corpseID = buf.readInt();
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.corpseID);
    }
}