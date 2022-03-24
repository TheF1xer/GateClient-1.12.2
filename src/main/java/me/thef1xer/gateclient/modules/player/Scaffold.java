package me.thef1xer.gateclient.modules.player;

import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Scaffold extends Module {
    public static final Scaffold INSTACE = new Scaffold();

    public Scaffold() {
        super("Scaffold", "scaffold", ModuleCategory.PLAYER);
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
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        BlockPos floorPos = new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ);

        // Does scaffold if the floor block can be replaced
        if (mc.world.getBlockState(floorPos).getMaterial().isReplaceable()) {

            // Save previous slot
            int prevSlot = mc.player.inventory.currentItem;
            ItemStack placeStack = mc.player.getHeldItemMainhand();

            // If the item held in the main hand is not a block, then find a block in the hot-bar
            if (!(placeStack.getItem() instanceof ItemBlock)) {

                for (int newSlot = 0; newSlot < 9; newSlot++) {
                    placeStack = mc.player.inventory.getStackInSlot(newSlot);

                    // If a block is found, select it
                    if (placeStack.getItem() instanceof ItemBlock) {
                        mc.player.inventory.currentItem = newSlot;
                        mc.playerController.updateController();
                        break;
                    }
                }

                // If no block was found, return
                if (!(placeStack.getItem() instanceof ItemBlock)) {
                    return;
                }
            }

            // Check all surrounding blocks
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offsetBlock = floorPos.offset(facing);

                // Check if block is solid
                if (mc.world.getBlockState(offsetBlock).getMaterial() != Material.AIR) {

                    // Create a hit vector that points to the center of the correct side of the block
                    Vec3d halfOppositeDirection = (new Vec3d(facing.getOpposite().getDirectionVec())).scale(0.5D);
                    Vec3d hitVec = new Vec3d(offsetBlock).addVector(0.5D + halfOppositeDirection.x, 0.5D + halfOppositeDirection.y, 0.5D + halfOppositeDirection.z);

                    // Rotate the player towards the block before placing the block
                    float[] facingRotations = PlayerUtil.getPlayerFacingRotations(hitVec.x, hitVec.y, hitVec.z);
                    mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, facingRotations[1], facingRotations[0], mc.player.onGround));

                    // Right-click the block to place it
                    if (mc.playerController.processRightClickBlock(mc.player, mc.world, offsetBlock, facing.getOpposite(), hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {

                        // If Right-click was successful, swing arm
                        mc.player.swingArm(EnumHand.MAIN_HAND);

                    }

                    event.setCanceled(true);

                    break;
                }
            }

            // Select the previously selected block
            if (prevSlot != mc.player.inventory.currentItem) {
                mc.player.inventory.currentItem = prevSlot;
                mc.playerController.updateController();
            }
        }
    }
}
