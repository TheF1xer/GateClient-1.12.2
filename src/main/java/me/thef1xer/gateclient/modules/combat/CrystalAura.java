package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
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

public class CrystalAura extends Module {
    public static CrystalAura INSTANCE = new CrystalAura();

    public final BooleanSetting targetFriends = new BooleanSetting("Target Friends", "targetfriends", false);
    public final BooleanSetting placeCrystal = new BooleanSetting("Place Crystal", "place", true);
    public final BooleanSetting breakCrystal = new BooleanSetting("Break Crystal", "break", true);
    public final FloatSetting placeDistance = new FloatSetting("Place Distance", "placedistance", 4.5F, 0, 6, 0.1F);
    public final FloatSetting breakDistance = new FloatSetting("Break Distance", "breakdistance", 4.5F, 0, 6, 0.1F);
    public final FloatSetting placeDelay = new FloatSetting("Place Delay", "placedelay", 1, 1, 10, 1);
    public final FloatSetting breakDelay = new FloatSetting("Break Delay", "breakdelay", 1, 1, 10, 1);
    public final BooleanSetting placeRaytrace = new BooleanSetting("Place Raytrace", "placeraytrace", false);
    public final BooleanSetting breakRaytrace = new BooleanSetting("Break Raytrace", "breakraytrace", false);

    private BlockPos lastPlacePos;
    private EntityEnderCrystal lastAttackedCrystal;
    private int ticksSinceLastPlace = 0;
    private int ticksSinceLastBreak = 0;

    private final Minecraft mc = Minecraft.getMinecraft();

    public CrystalAura() {
        super("Crystal Aura", "crystalaura", ModuleCategory.COMBAT);
        addSettings(targetFriends ,placeCrystal, breakCrystal, placeDistance, breakDistance, placeDelay, breakDelay, placeRaytrace, breakRaytrace);
    }

    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        ticksSinceLastPlace++;
        ticksSinceLastBreak++;
        float[] facingRotations = null;

        // Only continue if the current held item is an end crystal
        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            return;
        }

        // Place Crystal
        if (placeCrystal.getValue() && ticksSinceLastPlace >= placeDelay.getValue()) {

            // Select target player
            EntityPlayer target = getCrystalTarget();

            // Check if we found a valid target
            if (target != null) {

                // Get best pos to place a crystal
                BlockPos crystalFloorPos = getCrystalPlacePos(target);

                // Check if a crystal placement pos was found
                if (crystalFloorPos != null) {

                    // Create a hit vector that points to the center of the correct side of the block
                    Vec3d hitVec = new Vec3d(crystalFloorPos).addVector(0.5D, 1, 0.5D);

                    // Rotate the player towards the block before placing the block
                    facingRotations = PlayerUtil.getPlayerFacingRotations(hitVec.x, hitVec.y, hitVec.z);

                    // Right-click the block to place it
                    if (mc.playerController.processRightClickBlock(mc.player, mc.world, crystalFloorPos, EnumFacing.UP, hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS) {

                        // If Right-click was successful, swing arm
                        mc.player.swingArm(EnumHand.MAIN_HAND);

                    }

                    ticksSinceLastPlace = 0;
                    event.setCanceled(true);
                }
            }
        }

        // Break Crystal
        if (breakCrystal.getValue() && ticksSinceLastBreak >= breakDelay.getValue()) {

            // Select crystal to break
            EntityEnderCrystal enderCrystalToBreak = getEnderCrystalToBreak();

            if (enderCrystalToBreak != null) {

                // Rotate player towards the crystal
                facingRotations = PlayerUtil.getPlayerFacingRotations(enderCrystalToBreak.posX, enderCrystalToBreak.posY + enderCrystalToBreak.height / 2, enderCrystalToBreak.posZ);

                // Attack the crystal
                mc.player.connection.sendPacket(new CPacketUseEntity(enderCrystalToBreak));
                mc.player.swingArm(EnumHand.MAIN_HAND);

                ticksSinceLastBreak = 0;
                event.setCanceled(true);
            }
        }

        // Rotate Player

        // Some anticheats will not let the client send more than one rotation packet per tick and will break the module
        // This way only one packet will be sent. Needs more thought though
        if (facingRotations != null) {
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, facingRotations[1], facingRotations[0], mc.player.onGround));
        }
    }

    private EntityPlayer getCrystalTarget() {
        EntityPlayer target = null;

        for (EntityPlayer player : mc.world.playerEntities) {

            // Don't select the player as a target
            if (player == mc.player || player == mc.getRenderViewEntity()) {
                continue;
            }

            // Check friends
            if (!targetFriends.getValue()) {
                if (GateClient.getGate().friendsManager.isFriend(player.getName())) {
                    continue;
                }
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

                    // Check if we already tried to place a crystal in this position (to avoid spamming packets trying to place the crystal)
                    if (floorPos == lastPlacePos) {
                        continue;
                    }

                    // Check if we are inside the desired place distance
                    if (mc.player.getDistanceSqToCenter(floorPos) > placeDistance.getValue() * placeDistance.getValue()) {
                        continue;
                    }

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

        lastPlacePos = bestCrystalFloorPos;
        return bestCrystalFloorPos;
    }

    private EntityEnderCrystal getEnderCrystalToBreak() {
        EntityEnderCrystal enderCrystalToBreak = null;

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                EntityEnderCrystal enderCrystal = (EntityEnderCrystal) entity;

                float maxDist = breakDistance.getValue();

                // Check if we already tried to break this crystal (to avoid spamming packets trying to attack the crystal)
                if (enderCrystal == lastAttackedCrystal) {
                    continue;
                }

                // Check if entity can be seen (got this from the Minecraft source code)
                if (!mc.player.canEntityBeSeen(enderCrystal)) {
                    if (breakRaytrace.getValue()) {
                        continue;
                    }

                    // From the Minecraft Source code
                    maxDist = Math.min(maxDist, 3);
                }

                // Check if the crystal is in attack range
                if (mc.player.getDistanceSq(enderCrystal) < maxDist * maxDist) {

                    // Select the newest crystal in reach
                    if (enderCrystalToBreak == null || enderCrystal.ticksExisted < enderCrystalToBreak.ticksExisted) {
                        enderCrystalToBreak = enderCrystal;
                    }
                }
            }
        }

        lastAttackedCrystal = enderCrystalToBreak;
        return enderCrystalToBreak;
    }
}
