package me.thef1xer.gateclient.util;

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

    public static double[] getPlayerMoveVec() {
        float yaw = Minecraft.getMinecraft().player.rotationYaw;
        float forward = Minecraft.getMinecraft().player.moveForward;
        float strafe = Minecraft.getMinecraft().player.moveStrafing;

        //If the player is not moving, there is no move vector
        if (forward == 0 && strafe == 0) {
            return new double[] {0, 0};
        }

        //Change yaw depending on the direction the playing is travelling
        if (forward > 0) {

            //Moving forward
            if (strafe > 0) {
                yaw = yaw - 45;
            } else if (strafe < 0) {
                yaw = yaw + 45;
            }

        } else if (forward < 0) {

            //Moving backwards
            yaw = yaw - 180;
            if (strafe > 0) {
                yaw = yaw + 45;
            } else if (strafe < 0) {
                yaw = yaw - 45;
            }

        } else {

            //Not moving forward nor backwards
            if (strafe > 0) {
                yaw = yaw - 90;
            } else if (strafe < 0) {
                yaw = yaw + 90;
            }
        }

        return new double[] {-Math.sin(Math.toRadians(yaw)), Math.cos(Math.toRadians(yaw))};
    }
}
