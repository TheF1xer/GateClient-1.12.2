package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.util.MathUtil;
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
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
    public static final Tracers INSTANCE = new Tracers();

    public final BooleanSetting targetPlayer = new BooleanSetting("Players", "players", true);
    public final BooleanSetting targetHostile = new BooleanSetting("Monsters", "monsters", false);
    public final RGBSetting color = new RGBSetting("Tracer Color", "color", 255, 255, 255);
    public final FloatSetting colorAlpha = new FloatSetting("Color Alpha", "coloralpha", 1F, 0F, 1F);

    public Tracers() {
        super("Tracers", "tracers", Module.ModuleCategory.RENDER);
        targetPlayer.setParent("Entities to Target");
        targetHostile.setParent("Entities to Target");
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
        //TODO: Fix weird interaction with Storage ESP and Baritone
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            RenderManager rm = Minecraft.getMinecraft().getRenderManager();
            //TODO: Find a better method of doing this
            Vec3d playerVector = ActiveRenderInfo.getCameraPosition();

            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableDepth();
            GlStateManager.glLineWidth(0.5F);
            GlStateManager.color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, colorAlpha.getValue());

            for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                if (isTarget(entity) && entity != Minecraft.getMinecraft().getRenderViewEntity()) {
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    double[] entityPos = MathUtil.interpolateEntity(entity);

                    buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
                    buffer.pos(playerVector.x, playerVector.y, playerVector.z).endVertex();
                    buffer.pos(entityPos[0] - rm.viewerPosX, entityPos[1] - rm.viewerPosY, entityPos[2] - rm.viewerPosZ).endVertex();
                    tessellator.draw();
                }
            }
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
