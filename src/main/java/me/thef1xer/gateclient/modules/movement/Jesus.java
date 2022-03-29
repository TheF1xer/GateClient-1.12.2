package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.GetLiquidCollisionBoundingBoxEvent;
import me.thef1xer.gateclient.events.PlayerMoveEvent;
import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Jesus extends Module {

    public static final Jesus INSTANCE = new Jesus();

    public final FloatSetting offset = new FloatSetting("Offset", "offset", 0.2F, 0F, 1F);

    private final Minecraft mc = Minecraft.getMinecraft();

    public Jesus() {
        super("Jesus", "jesus", Module.ModuleCategory.MOVEMENT);
        this.addSettings(offset);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onLiquidCollision(GetLiquidCollisionBoundingBoxEvent event) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (mc.player.isSneaking() || mc.player.fallDistance > 3F || isInLiquid() || mc.player.motionY >= 0.1f) {
            return;
        }

        // Make the liquid act as a solid block if the block is bellow the player
        if (event.getBlockPos().getY() < mc.player.posY + offset.getValue()) {
            event.setCollisionBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D - offset.getValue(), 1.0D));
        }
    }

    @SubscribeEvent
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (isWaterWalking() && !mc.player.isSneaking()) {

            //This keeps the player constantly moving up and down to bypass some anticheats
            if (mc.player.ticksExisted % 2 == 0) {
                EntityPlayerSP player = mc.player;
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(player.posX, player.posY + offset.getValue(),
                        player.posZ, player.rotationYaw, player.rotationPitch, player.onGround));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onMove(PlayerMoveEvent event) {

        // Get the player out of the water
        if (isInLiquid() && !mc.player.isSneaking()) {
            event.y = 0.1D;
        }
    }

    private boolean isOverLiquid() {
        if (mc.player != null) {
            AxisAlignedBB playerBB = mc.player.getEntityBoundingBox();

            // Check if the player is above some liquid
            for (int x = MathHelper.floor(playerBB.minX); x < MathHelper.floor(playerBB.maxX) + 1; x++) {

                for (int z = MathHelper.floor(playerBB.minZ); z < MathHelper.floor(playerBB.minZ) + 1; z++) {

                    // Check a little bellow the player's feet
                    final Block block = mc.world.getBlockState(new BlockPos(x, playerBB.minY - 0.1D, z)).getBlock();

                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                }
            }

            return true;
        }
        return false;
    }

    private boolean isInLiquid() {
        AxisAlignedBB playerBB = mc.player.getEntityBoundingBox().offset(0, offset.getValue(), 0);

        // Check if the player's feet are inside a liquid
        for (int x = MathHelper.floor(playerBB.minX); x < MathHelper.ceil(playerBB.maxX); x++) {

            for (int z = MathHelper.floor(playerBB.minZ); z < MathHelper.ceil(playerBB.minZ); z++) {

                final Block block = mc.world.getBlockState(new BlockPos(x, playerBB.minY, z)).getBlock();

                if (block instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isWaterWalking() {
        return !isInLiquid() && isOverLiquid();
    }
}
