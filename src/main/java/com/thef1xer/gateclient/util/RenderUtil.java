package com.thef1xer.gateclient.util;

import com.thef1xer.gateclient.settings.impl.RGBSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
    public static void renderEntityBoundingBox(Entity entity, float red, float green, float blue, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        Vec3d entityPos = MathUtil.interpolateEntity(entity);

        AxisAlignedBB bb = new AxisAlignedBB(entityPos.x - entity.width/2, entityPos.y, entityPos.z - entity.width/2,
                entityPos.x + entity.width/2, entityPos.y + entity.height, entityPos.z + entity.width/2)
                .offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.drawSelectionBoundingBox(bb, red, green, blue, alpha);
    }

    public static void renderEntityBoundingBox(Entity entity, RGBSetting color, float alpha) {
        renderEntityBoundingBox(entity, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
    }

    public static void renderEntityFilledBoundingBox(Entity entity, float red, float green, float blue, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        Vec3d entityPos = MathUtil.interpolateEntity(entity);

        AxisAlignedBB bb = new AxisAlignedBB(entityPos.x - entity.width/2, entityPos.y, entityPos.z - entity.width/2,
                entityPos.x + entity.width/2, entityPos.y + entity.height, entityPos.z + entity.width/2)
                .offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.renderFilledBox(bb, red, green, blue, alpha);
    }

    public static void renderEntityFilledBoundingBox(Entity entity, RGBSetting color, float alpha) {
        renderEntityFilledBoundingBox(entity, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
    }

    public static void draw2DRect(double minX, double minY, double maxX, double maxY, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(red, green, blue, alpha);
        buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
        buffer.pos(minX, minY, 0D).endVertex();
        buffer.pos(minX, maxY, 0D).endVertex();
        buffer.pos(maxX, minY, 0D).endVertex();
        buffer.pos(maxX, maxY, 0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void draw2DRectLines(double minX, double minY, double maxX, double maxY, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(red, green, blue, alpha);
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        buffer.pos(minX, minY, 0D).endVertex();
        buffer.pos(minX, maxY, 0D).endVertex();
        buffer.pos(maxX, maxY, 0D).endVertex();
        buffer.pos(maxX, minY, 0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}