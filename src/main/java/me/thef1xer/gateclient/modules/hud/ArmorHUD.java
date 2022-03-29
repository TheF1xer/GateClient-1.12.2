package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorHUD extends Module {
    public static final ArmorHUD INSTANCE = new ArmorHUD();

    private final Minecraft mc = Minecraft.getMinecraft();

    public ArmorHUD() {
        super("Armor HUD", "armorhud", ModuleCategory.HUD);
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
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            int width = event.getResolution().getScaledWidth();
            int height = event.getResolution().getScaledHeight();


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
}
