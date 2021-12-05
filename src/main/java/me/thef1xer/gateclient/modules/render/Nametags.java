package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Nametags extends Module {
    public static final Nametags INSTANCE = new Nametags();

    private static final Minecraft mc = Minecraft.getMinecraft();

    public Nametags() {
        super("Nametags", "nametags", ModuleCategory.RENDER);
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
    public void onRenderName(RenderLivingEvent.Specials.Pre<?> event) {
        if (event.getEntity() instanceof EntityPlayer) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        for (EntityPlayer player : mc.world.playerEntities) {
            double[] pos = MathUtil.interpolateEntity(player);

            drawNametag(player, pos[0] - mc.getRenderManager().viewerPosX,
                    pos[1] - mc.getRenderManager().viewerPosY,
                    pos[2] - mc.getRenderManager().viewerPosZ);
        }
    }

    private void drawNametag(EntityPlayer player, double x, double y, double z) {
        FontRenderer fr = mc.fontRenderer;
        double dy = player.height + 0.5F - (player.isSneaking() ? 0.25F : 0.0F);
        boolean isThirdPersonFrontal = mc.gameSettings.thirdPersonView == 2;

        GlStateManager.pushMatrix();

        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.translate(x, y + dy, z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        fr.drawString(player.getDisplayNameString(), - (fr.getStringWidth(player.getDisplayNameString()) / 2) ,0, 0xFFFFFFFF);

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }
}
