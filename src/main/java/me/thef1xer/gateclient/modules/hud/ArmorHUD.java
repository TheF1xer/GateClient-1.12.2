package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends Module {
    public static final ArmorHUD INSTANCE = new ArmorHUD();

    private final Minecraft mc = Minecraft.getMinecraft();

    public ArmorHUD() {
        super("Armor HUD", "armorhud", ModuleCategory.HUD);
    }

    public void onRenderOverlay(ScaledResolution sr) {
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();


        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();

        int x = width / 2 + 74;

        // Render items in armor inventory
        for (ItemStack stack : mc.player.inventory.armorInventory) {
            if (!stack.isEmpty()) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, height - 58);
                mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, height - 58);
            }

            x -= 21;
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
