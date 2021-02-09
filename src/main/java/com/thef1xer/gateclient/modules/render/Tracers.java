package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.BooleanSetting;
import com.thef1xer.gateclient.settings.ColorSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Tracers extends Module {
    public Entity camera;

    public BooleanSetting targetPlayer = new BooleanSetting("Target Players", "targetplayers", true);
    public BooleanSetting targetHostile = new BooleanSetting("Target Hostile Mobs", "targethostile", false);
    public ColorSetting color = new ColorSetting("Tracer Color", "color", 255, 255, 255, 255);

    public Tracers() {
        super("Tracers", "tracers", EnumModuleCategory.RENDER);
        targetPlayer.setParent("Entities to Target");
        targetHostile.setParent("Entities to Target");
        this.addSettings(targetPlayer, targetHostile, color);

    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            RenderManager rm = Minecraft.getMinecraft().getRenderManager();
            camera = Minecraft.getMinecraft().getRenderViewEntity();

            Vec3d playerVector = new Vec3d(0D, 0D, 1D)
                    .rotatePitch((float) -Math.toRadians(camera.rotationPitch))
                    .rotateYaw((float) -Math.toRadians(camera.rotationYaw));

            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableDepth();
            for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                if (isTarget(entity) && entity != camera) {
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
                    buffer.pos(playerVector.x, playerVector.y + Minecraft.getMinecraft().player.eyeHeight, playerVector.z)
                            .color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255).endVertex();
                    buffer.pos(entity.posX - rm.viewerPosX, entity.posY - rm.viewerPosY, entity.posZ - rm.viewerPosZ)
                            .color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255).endVertex();
                    tessellator.draw();
                }
            }
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
    }

    public boolean isTarget(Entity entity) {
        if (targetPlayer.getValue() && entity instanceof EntityPlayer) {
            return true;
        } else if (targetHostile.getValue() && entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
            return true;
        }
        return false;
    }
}
