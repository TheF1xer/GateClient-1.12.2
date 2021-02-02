package com.thef1xer.gateclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;

public class PlayerUtil {

    //Not in use right now
    public static void swapItems(int slot1, int slot2) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot1, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot2, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot1, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.updateController();
    }

    /*
    Minecraft.getMinecraft().playerController.windowClick() won't work if you have a container opened, this is the code from that method but it only
    interacts with the player inventory :D
     */

    public static void swapInventoryItems(int slot1, int slot2) {
        short short1 = Minecraft.getMinecraft().player.inventoryContainer.getNextTransactionID(Minecraft.getMinecraft().player.inventory);

        ItemStack itemstack = Minecraft.getMinecraft().player.inventoryContainer.slotClick(slot1, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClickWindow(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot1, 0, ClickType.PICKUP, itemstack, short1));

        itemstack = Minecraft.getMinecraft().player.inventoryContainer.slotClick(slot2, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClickWindow(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot2, 0, ClickType.PICKUP, itemstack, short1));

        itemstack = Minecraft.getMinecraft().player.inventoryContainer.slotClick(slot1, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketClickWindow(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot1, 0, ClickType.PICKUP, itemstack, short1));

        Minecraft.getMinecraft().playerController.updateController();
    }
}
