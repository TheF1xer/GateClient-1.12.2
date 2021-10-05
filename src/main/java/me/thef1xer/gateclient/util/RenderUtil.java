package me.thef1xer.gateclient.util;

import me.thef1xer.gateclient.settings.impl.RGBSetting;
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

    public static void draw2DTriangleDown(double x, double y, double width, double height, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(red, green, blue, alpha);
        buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        buffer.pos(x, y, 0D).endVertex();
        buffer.pos(x + width / 2, y + height, 0D).endVertex();
        buffer.pos(x + width, y, 0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void draw2DTriangleRight(double x, double y, double width, double height, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(red, green, blue, alpha);
        buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        buffer.pos(x, y, 0D).endVertex();
        buffer.pos(x, y + height, 0D).endVertex();
        buffer.pos(x + width, y + height / 2, 0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void draw2DRect(double x, double y, double width, double height, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(red, green, blue, alpha);
        buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
        buffer.pos(x, y, 0D).endVertex();
        buffer.pos(x, y + height, 0D).endVertex();
        buffer.pos(x + width, y, 0D).endVertex();
        buffer.pos(x + width, y + height, 0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void draw2DRectLines(double x, double y, double width, double height, float red, float green, float blue, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.color(red, green, blue, alpha);
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        buffer.pos(x, y, 0D).endVertex();
        buffer.pos(x, y + height, 0D).endVertex();
        buffer.pos(x + width, y + height, 0D).endVertex();
        buffer.pos(x + width, y, 0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}