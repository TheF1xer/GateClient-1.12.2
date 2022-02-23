package me.thef1xer.gateclient.modules.player;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoArmor extends Module {
    public static final AutoArmor INSTANCE = new AutoArmor();

    public AutoArmor() {
        super("Auto Armor", "autoarmor", Module.ModuleCategory.PLAYER);
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
    public void onTick(TickEvent.ClientTickEvent event) {
        // Boots have index 0
        if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {
                return;
            }

            boolean hasBoots = !Minecraft.getMinecraft().player.inventory.armorInventory.get(0).isEmpty();
            boolean hasLeggings = !Minecraft.getMinecraft().player.inventory.armorInventory.get(1).isEmpty();
            boolean hasChestplate = !Minecraft.getMinecraft().player.inventory.armorInventory.get(2).isEmpty();
            boolean hasHelmet = !Minecraft.getMinecraft().player.inventory.armorInventory.get(3).isEmpty();

            // Only needed if at least one armor piece is missing
            if (!hasBoots || !hasLeggings || !hasChestplate || !hasHelmet) {
                for (int slot = 0; slot < Minecraft.getMinecraft().player.inventoryContainer.inventorySlots.size(); slot++) {

                    Item slotItem = Minecraft.getMinecraft().player.inventoryContainer.inventorySlots.get(slot).getStack().getItem();
                    if (slotItem instanceof ItemArmor) {
                        ItemArmor itemArmor = (ItemArmor) slotItem;
                        System.out.println(slot);

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
