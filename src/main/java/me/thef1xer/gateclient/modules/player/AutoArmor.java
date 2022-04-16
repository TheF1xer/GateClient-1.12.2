package me.thef1xer.gateclient.modules.player;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class AutoArmor extends Module {
    public static final AutoArmor INSTANCE = new AutoArmor();

    public AutoArmor() {
        super("Auto Armor", "autoarmor", Module.ModuleCategory.PLAYER);
    }

    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null && mc.player != null) {
            if (mc.currentScreen instanceof GuiContainer) {
                return;
            }

            // Boots have index 0
            boolean hasBoots = !mc.player.inventory.armorInventory.get(0).isEmpty();
            boolean hasLeggings = !mc.player.inventory.armorInventory.get(1).isEmpty();
            boolean hasChestplate = !mc.player.inventory.armorInventory.get(2).isEmpty();
            boolean hasHelmet = !mc.player.inventory.armorInventory.get(3).isEmpty();

            // Check if at least one armor piece is missing
            if (!hasBoots || !hasLeggings || !hasChestplate || !hasHelmet) {
                for (int slot = 0; slot < mc.player.inventoryContainer.inventorySlots.size(); slot++) {

                    Item slotItem = mc.player.inventoryContainer.inventorySlots.get(slot).getStack().getItem();
                    if (slotItem instanceof ItemArmor) {
                        ItemArmor itemArmor = (ItemArmor) slotItem;

                        if (!hasHelmet && itemArmor.armorType == EntityEquipmentSlot.HEAD) {
                            PlayerUtil.swapInventoryItems(slot, 5);
                            hasHelmet = true;
                        }

                        if (!hasChestplate && itemArmor.armorType == EntityEquipmentSlot.CHEST) {
                            PlayerUtil.swapInventoryItems(slot, 6);
                            hasChestplate = true;
                        }

                        if (!hasLeggings && itemArmor.armorType == EntityEquipmentSlot.LEGS) {
                            PlayerUtil.swapInventoryItems(slot, 7);
                            hasLeggings = true;
                        }

                        if (!hasBoots && itemArmor.armorType == EntityEquipmentSlot.FEET) {
                            PlayerUtil.swapInventoryItems(slot, 8);
                            hasBoots = true;
                        }
                    }
                }
            }
        }
    }
}
