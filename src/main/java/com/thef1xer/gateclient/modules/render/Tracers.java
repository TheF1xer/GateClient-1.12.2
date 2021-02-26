package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.BooleanSetting;
import com.thef1xer.gateclient.settings.impl.ColorSetting;
import com.thef1xer.gateclient.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
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
    public static Tracers INSTANCE;

    public final BooleanSetting targetPlayer = new BooleanSetting("Target Players", "targetplayers", true);
    public final BooleanSetting targetHostile = new BooleanSetting("Target Hostile Mobs", "targethostile", false);
    public final ColorSetting color = new ColorSetting("Tracer Color", "color", 255, 255, 255, 255);

    public Tracers() {
        super("Tracers", "tracers", EnumModuleCategory.RENDER);
        targetPlayer.setParent("Entities to Target");
        targetHostile.setParent("Entities to Target");
        this.addSettings(targetPlayer, targetHostile, color);

        Tracers.INSTANCE = this;

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
            //TODO: Find a better method of doing this
            Vec3d playerVector = ActiveRenderInfo.getCameraPosition();

            GlStateManager.pushMatrix();
            GlStateManager.clear(256);
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableDepth();
            GlStateManager.glLineWidth(0.5F);
            for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                if (isTarget(entity) && entity != Minecraft.getMinecraft().getRenderViewEntity()) {
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    Vec3d entityPos = MathUtil.interpolateEntity(entity);

                    buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
                    buffer.pos(playerVector.x, playerVector.y, playerVector.z)
                            .color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255).endVertex();
                    buffer.pos(entityPos.x - rm.viewerPosX, entityPos.y - rm.viewerPosY, entityPos.z - rm.viewerPosZ)
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
