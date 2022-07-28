package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.util.MathUtil;
import me.thef1xer.gateclient.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;

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

    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player != null && mc.world != null) {
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

                    // Draw tracer
                    double[] interpolatedEntityPos = MathUtil.interpolateEntity(entity);
                    RenderUtil.drawTracerFromPlayer(interpolatedEntityPos[0], interpolatedEntityPos[1], interpolatedEntityPos[2]);
                }
            }

            // Restore previous bobbing setting
            mc.gameSettings.viewBobbing = bobbing;
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
