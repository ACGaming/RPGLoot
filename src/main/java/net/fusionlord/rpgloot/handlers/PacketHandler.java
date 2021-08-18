package net.fusionlord.rpgloot.handlers;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    private final SimpleNetworkWrapper handler;

    private int packetCount = 0;

    public PacketHandler(String channelName)
    {
        this.handler = new SimpleNetworkWrapper(channelName);
    }

    public void registerMessage(Class handlerClass, Class packetClass, Side side)
    {
        this.handler.registerMessage(handlerClass, packetClass, this.packetCount++, side);
    }

    public void sendToServer(IMessage packet)
    {
        this.handler.sendToServer(packet);
    }

    public void sendToAllAround(IMessage packet, NetworkRegistry.TargetPoint targetPoint)
    {
        this.handler.sendToAllAround(packet, targetPoint);
    }
}