package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrystalAura extends Module {
    public static CrystalAura INSTANCE = new CrystalAura();

    private final BooleanSetting placeRaytrace = new BooleanSetting("Place Raytrace", "placeraytrace", false);
    private final BooleanSetting breakRaytrace = new BooleanSetting("Break Raytrace", "breakraytrace", false);

    private final Minecraft mc = Minecraft.getMinecraft();

    public CrystalAura() {
        super("Crystal Aura", "crystalaura", ModuleCategory.COMBAT);
        addSettings(placeRaytrace, breakRaytrace);
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

        // Only continue if the current held item is an end crystal
        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            return;
        }

        // Select target player
        EntityPlayer target = getCrystalTarget();

        // Check if we found a valid target
        // TODO: Choose distance more wisely
        if (target != null && mc.player.getDistanceSq(target) <= 36) {

            // Get best pos to place a crystal
            BlockPos bestCrystalFloorPos = getCrystalPlacePos(target);

            // Place Crystal

            // Check if a crystal placement pos was found
            if (bestCrystalFloorPos != null) {

                // Create a hit vector that points to the center of the correct side of the block
                Vec3d hitVec = new Vec3d(bestCrystalFloorPos).addVector(0.5D, 1, 0.5D);

                // Rotate the player towards the block before placing the block
                float[] facingRotations = PlayerUtil.getPlayerFacingRotations(hitVec.x, hitVec.y, hitVec.z);
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, facingRotations[1], facingRotations[0], mc.player.onGround));

                // Right-click the block to place it
                if (mc.playerController.processRightClickBlock(mc.player, mc.world, bestCrystalFloorPos, EnumFacing.UP, hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {

                    // If Right-click was successful, swing arm
                    mc.player.swingArm(EnumHand.MAIN_HAND);

                }

                event.setCanceled(true);
            }
        }

        // Break Crystal

        // Select crystal to break
        EntityEnderCrystal enderCrystalToBreak = getEnderCrystalToBreak();

        if (enderCrystalToBreak != null) {

            // Rotate player towards the crystal
            float[] facingRotations = PlayerUtil.getPlayerFacingRotations(enderCrystalToBreak.posX, enderCrystalToBreak.posY + enderCrystalToBreak.height/2, enderCrystalToBreak.posZ);
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, facingRotations[1], facingRotations[0], mc.player.onGround));

            // Attack the crystal
            mc.player.connection.sendPacket(new CPacketUseEntity(enderCrystalToBreak));
            mc.player.swingArm(EnumHand.MAIN_HAND);

            event.setCanceled(true);
        }
    }

    private EntityPlayer getCrystalTarget() {
        EntityPlayer target = null;

        for (EntityPlayer player : mc.world.playerEntities) {

            // Don't select the player as a target
            if (player == mc.player || player == mc.getRenderViewEntity()) {
                continue;
            }

            // Select closest player
            if (target == null) {
                target = player;
            } else  if (mc.player.getDistanceSq(target) < mc.player.getDistanceSq(player)) {
                target = player;
            }
        }

        return target;
    }

    private BlockPos getCrystalPlacePos(EntityPlayer target) {
        BlockPos bestCrystalFloorPos = null;
        double bestDamage = 0;

        int flooredX = MathHelper.floor(target.posX);
        int flooredY = MathHelper.floor(target.posY);
        int flooredZ = MathHelper.floor(target.posZ);

        // TODO: Choose better radius
        // Check blocks around the target
        for (int x = flooredX - 5; x <= flooredX + 5; x++) {
            for (int y = flooredY - 5; y <= flooredY + 5; y++) {
                for (int z = flooredZ - 5; z <= flooredZ + 5; z++) {

                    BlockPos crystalPos = new BlockPos(x, y, z);
                    BlockPos floorPos = crystalPos.down();

                    // Check if the block bellow allows for Crystal placement
                    if (mc.world.getBlockState(floorPos).getBlock() == Blocks.BEDROCK ||
                            mc.world.getBlockState(floorPos).getBlock() == Blocks.OBSIDIAN) {

                        // Check if the current block and the block above are empty
                        if (mc.world.getBlockState(crystalPos).getBlock() == Blocks.AIR ||
                                mc.world.getBlockState(crystalPos.up()).getBlock() == Blocks.AIR) {

                            //Check if there would be entities in the crystal AABB
                            boolean entityInBlockAABB = false;

                            AxisAlignedBB crystalBB = new AxisAlignedBB(
                                    crystalPos.getX(), crystalPos.getY(), crystalPos.getZ(),
                                    crystalPos.getX() + 1, crystalPos.getY() + 2, crystalPos.getZ() + 1
                            );

                            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, crystalBB)) {
                                if (entity.isDead) {
                                    continue;
                                }
                                entityInBlockAABB = true;
                                break;
                            }

                            if (entityInBlockAABB) {
                                continue;
                            }

                            // Raytrace check
                            if (placeRaytrace.getValue()) {

                                // We check a little bellow the top of the block to make sure it collides
                                RayTraceResult rayTraceResult = mc.world.rayTraceBlocks(mc.player.getPositionEyes(1), new Vec3d(floorPos).addVector(0.5D, 0.9, 0.5D));

                                // Might also need to test the facing
                                if (rayTraceResult == null || !rayTraceResult.getBlockPos().equals(floorPos)) {
                                    continue;
                                }
                            }


                            // We have to maximize (12 - distance) * exposure
                            // That is the variable part of the damage calculation of the crystal explosion
                            Vec3d crystalVec = new Vec3d(crystalPos).addVector(0.5F, 1, 0.5F);

                            double currentDamage = (12 - target.getDistance(crystalVec.x, crystalVec.y, crystalVec.z)) * mc.world.getBlockDensity(crystalVec, target.getEntityBoundingBox());


                            // If a block placement that deals more damage is found
                            if (currentDamage > bestDamage) {
                                bestCrystalFloorPos = floorPos;
                                bestDamage = currentDamage;
                            }
                        }
                    }
                }
            }
        }

        return bestCrystalFloorPos;
    }

    private EntityEnderCrystal getEnderCrystalToBreak() {
        EntityEnderCrystal enderCrystalToBreak = null;

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                EntityEnderCrystal enderCrystal = (EntityEnderCrystal) entity;
                int maxDist = 36;

                // Check if entity can be seen (got this from the Minecraft source code)
                if (breakRaytrace.getValue() && !mc.player.canEntityBeSeen(enderCrystal)) {
                    maxDist = 9;
                }

                // Check if the crystal is in attack range
                if (mc.player.getDistanceSq(enderCrystal) < maxDist) {

                    // Select the newest crystal in reach
                    if (enderCrystalToBreak == null || enderCrystal.ticksExisted < enderCrystalToBreak.ticksExisted) {
                        enderCrystalToBreak = enderCrystal;
                    }
                }
            }
        }

        return enderCrystalToBreak;
    }
}
