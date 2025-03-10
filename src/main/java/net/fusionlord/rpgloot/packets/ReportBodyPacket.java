package net.fusionlord.rpgloot.packets;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;
import net.fusionlord.rpgloot.client.events.RPGSoundEvents;

public class ReportBodyPacket implements IMessage
{
    public ReportBodyPacket()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    public static class HANDLER implements IMessageHandler<ReportBodyPacket, IMessage>
    {
        @Override
        public IMessage onMessage(ReportBodyPacket message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            World world = player.getEntityWorld();
            List<EntityPlayerMP> playersMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
            for (EntityPlayerMP playerMP : playersMP)
            {
                playerMP.sendMessage(new TextComponentString("Dead body reported at X: " + Math.round(player.posX) + " Y: " + Math.round(player.posY) + " Z: " + Math.round(player.posZ)));
                world.playSound(null, player.getPosition(), RPGSoundEvents.LOOTING_REPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            return null;
        }
    }
}