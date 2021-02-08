package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.BooleanSetting;
import com.thef1xer.gateclient.settings.ColorSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityESP extends Module {
    BooleanSetting targetPlayer = new BooleanSetting("Target Players", "targetplayer", true);
    BooleanSetting targetHostile = new BooleanSetting("Target Hostile", "targethostile", true);

    ColorSetting playerColor = new ColorSetting("Player Color", "playercolor", 255, 0, 0, 255);
    ColorSetting hostileColor = new ColorSetting("Hostile Mobs Color", "hostilecolor", 255, 255, 0, 255);

    public EntityESP() {
        super("Entity ESP", "entityesp", EnumModuleCategory.RENDER);
        targetPlayer.setParent("Target");
        targetHostile.setParent("Target");
        playerColor.setParent("Color");
        hostileColor.setParent("Color");
        this.addSettings(targetPlayer, targetHostile, playerColor, hostileColor);
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
    public void onRenderWorld(RenderWorldLastEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableDepth();

        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            AxisAlignedBB bb;
            if (targetPlayer.getValue() && entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().getRenderViewEntity()) {
                bb = entity.getEntityBoundingBox().offset(-Minecraft.getMinecraft().getRenderManager().viewerPosX, -Minecraft.getMinecraft().getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
                RenderGlobal.drawSelectionBoundingBox(bb, (float) playerColor.getRed()/255, (float) playerColor.getGreen()/255, (float) playerColor.getBlue()/255, (float) playerColor.getAlpha()/255);
            } else if (targetHostile.getValue() && entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
                bb = entity.getEntityBoundingBox().offset(-Minecraft.getMinecraft().getRenderManager().viewerPosX, -Minecraft.getMinecraft().getRenderManager().viewerPosY, -Minecraft.getMinecraft().getRenderManager().viewerPosZ);
                RenderGlobal.drawSelectionBoundingBox(bb, (float) hostileColor.getRed()/255, (float) hostileColor.getGreen()/255, (float) hostileColor.getBlue()/255, (float) hostileColor.getAlpha()/255);
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
}
