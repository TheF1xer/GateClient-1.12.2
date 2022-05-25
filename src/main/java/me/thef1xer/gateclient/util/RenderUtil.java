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
    public static void drawTracerFromPlayer(double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager rm = mc.getRenderManager();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Vector where the tracer starts
        Vec3d cameraVector = new Vec3d(0D, 0D, 1D).
                rotatePitch((float) - Math.toRadians(mc.getRenderViewEntity().rotationPitch)).
                rotateYaw((float) - Math.toRadians(mc.getRenderViewEntity().rotationYaw)).
                addVector(0D, mc.getRenderViewEntity().getEyeHeight(), 0D);

        // Draw the tracer
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        buffer.pos(cameraVector.x, cameraVector.y, cameraVector.z).endVertex();
        buffer.pos(x - rm.viewerPosX, y - rm.viewerPosY, z - rm.viewerPosZ).endVertex();
        tessellator.draw();
    }

    public static void renderEntityBoundingBox(Entity entity, float red, float green, float blue, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        double[] entityPos = MathUtil.interpolateEntity(entity);
        double x = entityPos[0];
        double y = entityPos[1];
        double z = entityPos[2];

        AxisAlignedBB bb = new AxisAlignedBB(x - entity.width/2, y, z - entity.width/2,
                x + entity.width/2, y + entity.height, z + entity.width/2)
                .offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

        RenderGlobal.drawSelectionBoundingBox(bb, red, green, blue, alpha);
    }

    public static void renderEntityBoundingBox(Entity entity, RGBSetting color, float alpha) {
        renderEntityBoundingBox(entity, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
    }

    public static void renderEntityFilledBoundingBox(Entity entity, float red, float green, float blue, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        double[] entityPos = MathUtil.interpolateEntity(entity);
        double x = entityPos[0];
        double y = entityPos[1];
        double z = entityPos[2];

        AxisAlignedBB bb = new AxisAlignedBB(x - entity.width/2, y, z - entity.width/2,
                x + entity.width/2, y + entity.height, z + entity.width/2)
                .offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);

        RenderGlobal.renderFilledBox(bb, red, green, blue, alpha);
    }

    public static void renderEntityFilledBoundingBox(Entity entity, RGBSetting color, float alpha) {
        renderEntityFilledBoundingBox(entity, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
    }

    public static void draw2DTriangleDown(double x, double y, double width, double height, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
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
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
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