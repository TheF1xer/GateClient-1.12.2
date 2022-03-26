package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Surround extends Module {
    public static final Surround INSTANCE = new Surround();

    public final BooleanSetting center = new BooleanSetting("Center", "center", true);
    public final FloatSetting blocksPerTick = new FloatSetting("Blocks Per Tick", "blocks", 4, 1, 4, 1);

    private final Minecraft mc = Minecraft.getMinecraft();

    public Surround() {
        super("Surround", "surround", ModuleCategory.COMBAT);
        addSettings(center, blocksPerTick);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register(this);

        if (center.getValue()) {
            if (mc.player == null || mc.world == null) {
                return;
            }

            // Center player
            mc.player.setPosition(
                    Math.floor(mc.player.posX) + 0.5D,
                    Math.floor(mc.player.posY),
                    Math.floor(mc.player.posZ) + 0.5D
            );
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        BlockPos playerPos = new BlockPos(mc.player);
        int blocksThisTick = 0;

        // Save previous slot
        int prevSlot = mc.player.inventory.currentItem;
        ItemStack placeStack = mc.player.getHeldItemMainhand();

        // If the item held in the main hand is not obsidian, then find obsidian in the hot-bar
        if (placeStack.getItem() != Item.getItemFromBlock(Blocks.OBSIDIAN)) {

            for (int newSlot = 0; newSlot < 9; newSlot++) {
                placeStack = mc.player.inventory.getStackInSlot(newSlot);

                // If obsidian is found, select it
                if (placeStack.getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                    mc.player.inventory.currentItem = newSlot;
                    mc.playerController.updateController();
                    break;
                }
            }

            // If no obsidian was found, return
            if (placeStack.getItem() != Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                return;
            }
        }


        // Searching and placing the surrounding blocks
        for (EnumFacing aroundPlayer : EnumFacing.HORIZONTALS) {
            BlockPos surroundPos = playerPos.offset(aroundPlayer);

            // Check if blocks around player are replaceable
            if (mc.world.getBlockState(surroundPos).getMaterial().isReplaceable()) {

                // Check for blocks around the desired new block
                for (EnumFacing aroundBlock : EnumFacing.values()) {
                    BlockPos placePos = surroundPos.offset(aroundBlock);

                    // Check if block is solid
                    if (mc.world.getBlockState(placePos).getMaterial() != Material.AIR) {

                        // Create a hit vector that points to the center of the correct side of the block
                        Vec3d halfOppositeDirection = (new Vec3d(aroundBlock.getOpposite().getDirectionVec())).scale(0.5D);
                        Vec3d hitVec = new Vec3d(placePos).addVector(0.5D + halfOppositeDirection.x, 0.5D + halfOppositeDirection.y, 0.5D + halfOppositeDirection.z);

                        // Rotate the player towards the block before placing the block
                        float[] facingRotations = PlayerUtil.getPlayerFacingRotations(hitVec.x, hitVec.y, hitVec.z);
                        mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, facingRotations[1], facingRotations[0], mc.player.onGround));

                        // Right-click the block to place it
                        if (mc.playerController.processRightClickBlock(mc.player, mc.world, placePos, aroundBlock.getOpposite(), hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {

                            // If Right-click was successful, swing arm
                            mc.player.swingArm(EnumHand.MAIN_HAND);

                            // Increment blocks per tick counter
                            blocksThisTick++;
                        }

                        event.setCanceled(true);

                        break;
                    }
                }

                // Don't place more blocks if the amount of desired blocks per tick is reached
                if (blocksThisTick >= (int) blocksPerTick.getValue()) {
                    break;
                }
            }
        }


        // Select the previously selected item
        if (prevSlot != mc.player.inventory.currentItem) {
            mc.player.inventory.currentItem = prevSlot;
            mc.playerController.updateController();
        }
    }
}
