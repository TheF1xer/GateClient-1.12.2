package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.MathUtil;
import me.thef1xer.gateclient.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

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
            if (player == mc.getRenderViewEntity() || !player.isEntityAlive()) continue;

            double[] pos = MathUtil.interpolateEntity(player);

            drawNametag(player, pos[0] - mc.getRenderManager().viewerPosX,
                    pos[1] - mc.getRenderManager().viewerPosY,
                    pos[2] - mc.getRenderManager().viewerPosZ);
        }
    }

    private void drawNametag(EntityPlayer player, double x, double y, double z) {
        FontRenderer fr = mc.fontRenderer;
        double distance = mc.getRenderViewEntity().getDistance(player);
        double distanceAbovePlayer = player.height + 0.5F - (player.isSneaking() ? 0.25F : 0.0F);
        boolean isThirdPersonFrontal = mc.gameSettings.thirdPersonView == 2;

        //Build the Ping - Name - Health String
        int ping = 0;
        if (mc.getConnection().getPlayerInfo(player.getUniqueID()) != null) {
            ping = mc.getConnection().getPlayerInfo(player.getUniqueID()).getResponseTime();
        }

        String nameHealthPing = TextFormatting.GRAY.toString() + ping + "ms " +
                TextFormatting.RESET.toString() + player.getDisplayNameString() + " " +
                TextFormatting.GREEN.toString() + (int) (player.getHealth() + player.getAbsorptionAmount());

        int stringWidth = fr.getStringWidth(nameHealthPing);


        //Drawing
        GlStateManager.pushMatrix();

        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        //Transformations
        GlStateManager.translate(x, y + distanceAbovePlayer, z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

        //Scale the name if distance of the entity to the player is more than 10 blocks
        if (distance > 10) {
            GlStateManager.scale(-0.025F * distance / 10, -0.025F * distance / 10, 0.025F * distance / 10);
        } else {
            GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        }

        //Draw Background Rectangle
        RenderUtil.draw2DRect(- (float) (stringWidth / 2) - 2, - 2, stringWidth + 3, 12, 0, 0, 0, 0.5F);

        //Draw Player Name Health and Ping
        fr.drawString(nameHealthPing, - (float) (stringWidth / 2),0, 0xFFFFFFFF, false);

        //Draw armor and hands
        GlStateManager.pushMatrix();
        //Weird fix to a rotation bug that was happening
        GlStateManager.scale(1, 1, 0.001F);
        RenderHelper.enableGUIStandardItemLighting();

        //Main Hand
        ItemStack mainHand = player.getHeldItemMainhand();
        if (!mainHand.isEmpty()) {
            renderItem(mainHand, -51, -18);
        }

        //Armor
        int itemPosX = 19;
        for (ItemStack armorStack : player.inventory.armorInventory) {
            if (!armorStack.isEmpty()) {
                renderItem(armorStack, itemPosX, -18);
            }
            itemPosX -= 18;
        }

        //Offhand
        ItemStack offHand = player.getHeldItemOffhand();
        if (!offHand.isEmpty()) {
            renderItem(offHand, 35, -18);
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();

        GlStateManager.popMatrix();
    }

    private void renderItem(ItemStack item, int posX, int posY) {
        //TODO: Fix item lighting
        //This makes the items visible through walls with depth enabled
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, posX, posY);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer ,item, posX, posY);
        GlStateManager.disableDepth();
    }
}
