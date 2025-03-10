package net.fusionlord.rpgloot.client.rendering;

import java.util.UUID;
import javax.annotation.Nonnull;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.mojang.authlib.GameProfile;
import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.config.RPGConfig;
import net.fusionlord.rpgloot.entities.EntityCorpse;

public class CorpseRenderFactory implements IRenderFactory<EntityCorpse>
{
    public Render<? super EntityCorpse> createRenderFor(RenderManager manager)
    {
        return new RenderCorpse(manager);
    }

    private static class RenderCorpse extends Render<EntityCorpse>
    {
        RenderCorpse(RenderManager renderManager)
        {
            super(renderManager);
        }

        @Override
        public void doRender(@Nonnull EntityCorpse corpse, double x, double y, double z, float whoknows, float partialTicks)
        {
            try
            {
                Entity entInstance = null;
                String entClass = corpse.getEntityClass();
                if (entClass.contains("EntityPlayerMP"))
                {
                    UUID playerID = new UUID(corpse.getOldEntityData().getLong("UUIDMost"), corpse.getOldEntityData().getLong("UUIDLeast"));
                    EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(corpse.world, new GameProfile(playerID, ""));
                    entityOtherPlayerMP.setSneaking(true);
                }
                else
                {
                    Class<?> entClazz = Class.forName(entClass);
                    entInstance = (Entity) entClazz.getConstructor(World.class).newInstance(corpse.world);
                    if (corpse.getOldEntityData() != null)
                    {
                        entInstance.readFromNBT(corpse.getOldEntityData());
                    }
                }
                if (entInstance != null)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x, (float) y, (float) z);
                    GlStateManager.translate(0.0D, ((entInstance.getEntityBoundingBox()).maxX - (entInstance.getEntityBoundingBox()).minX) / 2.0D, 0.0D);
                    GlStateManager.rotate((int) entInstance.prevRotationYaw, 0.0F, 1.0F, 0.0F);
                    if ((entInstance.getEntityBoundingBox()).maxY > 1.5D) GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                    if (entInstance.width > entInstance.height)
                    {
                        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                    }
                    else if (entInstance instanceof EntityAnimal || entInstance instanceof EntityAmbientCreature)
                    {
                        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.translate((float) ((entInstance.getEntityBoundingBox()).maxZ - (entInstance.getEntityBoundingBox()).minZ) / 2.0F, 0.0F, 0.0F);
                    }
                    else if (entInstance instanceof EntityOtherPlayerMP)
                    {
                        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                        GlStateManager.translate(0.0F, 0.0F, (float) ((entInstance.getEntityBoundingBox()).maxZ - (entInstance.getEntityBoundingBox()).minZ) / 2.0F);
                    }
                    GlStateManager.translate(0.0F, (float) -((entInstance.getEntityBoundingBox()).maxY - (entInstance.getEntityBoundingBox()).minY) / 2.0F, 0.0F);
                    this.renderManager.setRenderShadow(false);
                    this.renderManager.renderEntity(entInstance, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
                    GlStateManager.popMatrix();
                }
            }
            catch (Exception exception)
            {
                if (RPGConfig.debug.enableDebugLogging)
                {
                    RPGLoot.logger.error(exception);
                }
            }
        }

        protected ResourceLocation getEntityTexture(@Nonnull EntityCorpse corpse)
        {
            return null;
        }

        @Override
        public void doRenderShadowAndFire(@Nonnull Entity corpse, double x, double y, double z, float yaw, float partialTicks) {}
    }
}