package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
    public static final Tracers INSTANCE = new Tracers();

    public final BooleanSetting targetPlayer = new BooleanSetting("Players", "players", true);
    public final BooleanSetting targetHostile = new BooleanSetting("Monsters", "monsters", false);
    public final RGBSetting color = new RGBSetting("Tracer Color", "color", 255, 255, 255);
    public final FloatSetting colorAlpha = new FloatSetting("Color Alpha", "coloralpha", 1F, 0F, 1F);

    public Tracers() {
        super("Tracers", "tracers", Module.ModuleCategory.RENDER);
        this.addSettings(targetPlayer, targetHostile, color, colorAlpha);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player != null && mc.world != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableDepth();
            GlStateManager.glLineWidth(0.5F);
            GlStateManager.color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, colorAlpha.getValue());

            // Cancel the player's bobbing
            boolean bobbing = mc.gameSettings.viewBobbing;
            mc.gameSettings.viewBobbing = false;
            GlStateManager.loadIdentity();
            mc.entityRenderer.orientCamera(event.getPartialTicks());

            // Loop through all entities
            for (Entity entity : mc.world.loadedEntityList) {
                if (isTarget(entity) && entity != mc.getRenderViewEntity()) {

                    RenderManager rm = mc.getRenderManager();
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();

                    // Vector where the tracer starts
                    Vec3d cameraVector = new Vec3d(0D, 0D, 1D).
                            rotatePitch((float) - Math.toRadians(mc.getRenderViewEntity().rotationPitch)).
                            rotateYaw((float) - Math.toRadians(mc.getRenderViewEntity().rotationYaw)).
                            addVector(0D, mc.getRenderViewEntity().getEyeHeight(), 0D);

                    // Interpolate the target's position
                    double[] interpolatedEntityPos = MathUtil.interpolateEntity(entity);

                    // Draw the tracer
                    buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
                    buffer.pos(cameraVector.x, cameraVector.y, cameraVector.z).endVertex();
                    buffer.pos(interpolatedEntityPos[0] - rm.viewerPosX, interpolatedEntityPos[1] - rm.viewerPosY, interpolatedEntityPos[2] - rm.viewerPosZ).endVertex();
                    tessellator.draw();
                }
            }

            // Restore previous bobbing setting
            mc.gameSettings.viewBobbing = bobbing;

            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
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
