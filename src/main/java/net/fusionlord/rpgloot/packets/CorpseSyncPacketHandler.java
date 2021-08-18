package net.fusionlord.rpgloot.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CorpseSyncPacketHandler implements IMessageHandler<CorpseSyncPacket, IMessage>
{
    public IMessage onMessage(CorpseSyncPacket message, MessageContext ctx)
    {
        WorldClient worldClient = (Minecraft.getMinecraft()).world;
        Entity entity = worldClient.getEntityByID(message.corpseID);
        if (entity instanceof net.fusionlord.rpgloot.entities.EntityCorpse)
            entity.readFromNBT(message.corpseTag);
        return null;
    }
}