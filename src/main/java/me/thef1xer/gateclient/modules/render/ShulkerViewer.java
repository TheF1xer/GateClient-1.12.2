package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.events.RenderToolTipEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class ShulkerViewer extends Module {
    public static final ShulkerViewer INSTANCE = new ShulkerViewer();

    public ShulkerViewer() {
        super("ShulkerViewer", "shulkerviewer", ModuleCategory.RENDER);
    }

    public void onRenderToolTip(RenderToolTipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemShulkerBox) {
            Minecraft mc = Minecraft.getMinecraft();
            FontRenderer fr = mc.fontRenderer;

            ItemStack shulkerStack = event.getItemStack();
            NBTTagCompound tagCompound = shulkerStack.getTagCompound();

            // This stack means that the Shulker Box has an inventory
            if (tagCompound != null && tagCompound.hasKey("BlockEntityTag")) {
                int shulkerX = event.getX();
                int shulkerY = event.getY();

                // Create list with all the items in the Shulker
                NonNullList<ItemStack> itemList = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(tagCompound.getCompoundTag("BlockEntityTag"), itemList);

                mc.getRenderItem().zLevel = 300;

                // Draw container background
                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                RenderUtil.draw2DRect(shulkerX, shulkerY, 176, 72, 0, 0, 0, 0.6F);

                // Draw Shulker's display name
                fr.drawString(shulkerStack.getDisplayName(), shulkerX + 8, shulkerY + 6, 0xFFFFFF);

                GlStateManager.enableDepth();
                RenderHelper.enableGUIStandardItemLighting();

                // Draw items in Shulker Box
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 9; x++) {
                        ItemStack itemStack = itemList.get(y * 9 + x);
                        int itemX = shulkerX + 8 + x * 18;
                        int itemY = shulkerY + 16 + y * 18;

                        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, itemX, itemY);
                        mc.getRenderItem().renderItemOverlays(mc.fontRenderer ,itemStack, itemX, itemY);
                    }
                }

                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();

                // Cancel normal Tooltip drawing
                event.setCanceled(true);
            }
        }
    }
}
