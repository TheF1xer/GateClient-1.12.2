package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class AutoTotem extends Module {
    public static final AutoTotem INSTANCE = new AutoTotem();

    public AutoTotem() {
        super("Auto Totem", "autototem", Module.ModuleCategory.COMBAT);
    }

    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();

        // Stop if the player opened a container
        if (mc.currentScreen instanceof GuiContainer) {
            return;
        }

        if (mc.world != null && mc.player != null) {

            // Check if player doesn't have a totem in their off-hand
            if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {

                // Search for a totem
                for (int slot = 0; slot < mc.player.inventoryContainer.inventorySlots.size(); slot++) {
                    ItemStack itemStack = mc.player.inventoryContainer.inventorySlots.get(slot).getStack();

                    // Check if totem is found
                    if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {

                        // Put totem in the off-hand
                        PlayerUtil.swapInventoryItems(slot, 45);
                        break;
                    }
                }
            }
        }
    }
}
