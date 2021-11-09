package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.GetLiquidCollisionBoundingBoxEvent;
import me.thef1xer.gateclient.events.SendPacketEvent;
import me.thef1xer.gateclient.events.PlayerMoveEvent;
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

        //Makes the liquid act as a solid block (my need testing in some edge cases)
        event.setCollisionBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D - offset.getValue(), 1.0D));
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            if (isWaterWalking() && !mc.player.isSneaking()) {

                //This keeps the player constantly moving up and down to bypass some anticheats
                if (mc.player.ticksExisted % 2 == 0) {
                    EntityPlayerSP player = mc.player;
                    event.setPacket(new CPacketPlayer.PositionRotation(player.posX, player.posY + offset.getValue(),
                            player.posZ, player.rotationYaw, player.rotationPitch, player.onGround));
                }
            }
        }
    }

    @SubscribeEvent
    public void onMove(PlayerMoveEvent event) {
        //This gets the player out of the water
        if (isInLiquid() && !mc.player.isSneaking()) {
            event.y = 0.1D;
        }
    }

    private boolean isOverLiquid() {
        if (mc.player != null) {
            AxisAlignedBB bb = mc.player.getEntityBoundingBox().offset(0, - 0.1D, 0);

            for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
                for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.minZ) + 1; z++) {
                    final Block block = mc.world.getBlockState(new BlockPos(x, bb.minY, z)).getBlock();

                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                }
            }

            return true;
        }
        return false;
    }

    private boolean isWaterWalking() {
        return !isInLiquid()
                && isOverLiquid();
    }

    private boolean isInLiquid() {
        AxisAlignedBB bb = mc.player.getEntityBoundingBox().offset(0, offset.getValue(), 0);
        for (int x = MathHelper.floor(bb.minX); x < MathHelper.ceil(bb.maxX); x++) {
            for (int z = MathHelper.floor(bb.minZ); z < MathHelper.ceil(bb.minZ); z++) {
                final Block block = mc.world.getBlockState(new BlockPos(x, bb.minY, z)).getBlock();

                if (block instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
}
