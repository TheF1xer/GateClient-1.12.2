package com.thef1xer.gateclient.util;

import com.thef1xer.gateclient.settings.impl.ColorSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class RenderUtil {
    public static void renderEntityBoundingBox(Entity entity, float red, float green, float blue, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        Vec3d entityPos = MathUtil.interpolateEntity(entity);

        AxisAlignedBB bb = new AxisAlignedBB(entityPos.x - entity.width/2, entityPos.y, entityPos.z - entity.width/2,
                entityPos.x + entity.width/2, entityPos.y + entity.height, entityPos.z + entity.width/2)
                .offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.drawSelectionBoundingBox(bb, red, green, blue, alpha);
    }

    public static void renderEntityBoundingBox(Entity entity, ColorSetting color) {
        renderEntityBoundingBox(entity, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }
}