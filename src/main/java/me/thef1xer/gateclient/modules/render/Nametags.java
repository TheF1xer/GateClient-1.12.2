package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
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
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
    public static final Nametags INSTANCE = new Nametags();

    public final BooleanSetting drawHealth = new BooleanSetting("Health", "health", true);
    public final BooleanSetting drawPing = new BooleanSetting("Ping", "ping", true);
    public final BooleanSetting drawInventory = new BooleanSetting("Inventory", "inventory", true);

    private final Minecraft mc = Minecraft.getMinecraft();

    public Nametags() {
        super("Nametags", "nametags", ModuleCategory.RENDER);
        addSettings(drawHealth, drawPing, drawInventory);
    }

    public void onRenderName(RenderLivingEvent.Specials.Pre<?> event) {
        if (event.getEntity() instanceof EntityPlayer) {
            event.setCanceled(true);
        }
    }

    public void onRenderWorldLast() {
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


        // Build the Ping - Name - Health String
        String nameHealthPing = "";

        if (drawPing.getValue()) {
            int ping = 0;
            if (mc.getConnection().getPlayerInfo(player.getUniqueID()) != null) {
                ping = mc.getConnection().getPlayerInfo(player.getUniqueID()).getResponseTime();
            }

            nameHealthPing = TextFormatting.GRAY.toString() + ping + "ms ";
        }

        nameHealthPing = nameHealthPing + TextFormatting.RESET + player.getDisplayNameString();

        if (drawHealth.getValue()) {
            nameHealthPing = nameHealthPing + " " + TextFormatting.GREEN + (int) (player.getHealth() + player.getAbsorptionAmount());
        }

        int stringWidth = fr.getStringWidth(nameHealthPing);


        // Drawing
        GlStateManager.pushMatrix();

        // Transformations
        GlStateManager.translate(x, y + distanceAbovePlayer, z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

        // Scale the name if distance of the entity to the player is more than 10 blocks
        if (distance > 10) {
            GlStateManager.scale(-0.025F * distance / 10, -0.025F * distance / 10, 0.025F * distance / 10);
        } else {
            GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        }

        // Draw Background Rectangle
        RenderUtil.draw2DRect(- (float) (stringWidth / 2) - 2, - 2, stringWidth + 3, 12, 0, 0, 0, 0.5F);
        GlStateManager.enableTexture2D();

        // Draw Player Name Health and Ping
        if (GateClient.getGate().friendsManager.isFriend(player.getName())) {
            // Friends should have cyan name
            fr.drawString(nameHealthPing, -(float) (stringWidth / 2), 0, 0x00FFFF, false);
        } else {
            // White name for everyone else
            fr.drawString(nameHealthPing, -(float) (stringWidth / 2), 0, 0xFFFFFF, false);
        }

        // Draw armor and hands
        if (drawInventory.getValue()) {
            GlStateManager.pushMatrix();

            //Weird fix to a rotation bug that was happening
            GlStateManager.scale(1, 1, 0.001F);
            RenderHelper.enableGUIStandardItemLighting();

            // Main Hand
            ItemStack mainHand = player.getHeldItemMainhand();
            if (!mainHand.isEmpty()) {
                renderItem(mainHand, -51, -18);
            }

            // Armor
            int itemPosX = 19;
            for (ItemStack armorStack : player.inventory.armorInventory) {
                if (!armorStack.isEmpty()) {
                    renderItem(armorStack, itemPosX, -18);
                }
                itemPosX -= 18;
            }

            // Offhand
            ItemStack offHand = player.getHeldItemOffhand();
            if (!offHand.isEmpty()) {
                renderItem(offHand, 35, -18);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }

        GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();
    }

    private void renderItem(ItemStack item, int posX, int posY) {
        // TODO: Fix item lighting
        // This makes the items visible through walls with depth enabled
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, posX, posY);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer ,item, posX, posY);
        GlStateManager.disableDepth();
    }
}
