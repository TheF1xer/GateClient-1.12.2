package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class KillAura extends Module {
    public static final KillAura INSTANCE = new KillAura();

    public final EnumSetting priority = new EnumSetting("Priority", "priority", Priority.values(), Priority.CLOSEST);
    public final FloatSetting reach = new FloatSetting("Reach", "reach", 3F, 0F, 6F);
    public final FloatSetting delay = new FloatSetting("Added Delay" , "delay", 0F);
    public final BooleanSetting targetFriends = new BooleanSetting("Target Friends", "targetfriends", false);
    public final BooleanSetting players = new BooleanSetting("Players", "players", true);
    public final BooleanSetting monsters = new BooleanSetting("Monsters", "monsters", true);
    public final BooleanSetting passives = new BooleanSetting("Passives", "passives", false);

    private Entity lastAttackedEntity;

    private final Minecraft mc = Minecraft.getMinecraft();

    public KillAura() {
        super("Kill Aura", "killaura", Module.ModuleCategory.COMBAT);
        this.addSettings(priority, reach, delay, targetFriends, players, monsters, passives);
    }

    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        Entity target = null;

        // Select target
        if (priority.getCurrentValue() == Priority.CLOSEST) {

            // Loop through entities and select the closest valid target
            for (Entity entity : mc.world.loadedEntityList) {
                if (isValidTarget(entity)) {
                    if (target == null) {
                        target = entity;
                        continue;
                    }

                    if (mc.player.getDistanceSq(entity) > mc.player.getDistanceSq(target)) {
                        target = entity;
                    }
                }
            }
        } else if (priority.getCurrentValue() == Priority.FOCUS) {

            // Check if last attacked entity is loaded
            if (mc.world.loadedEntityList.contains(lastAttackedEntity)) {
                if (isValidTarget(lastAttackedEntity)) {
                    target = lastAttackedEntity;
                }
            }
        }

        // Attack the target
        if (target != null) {
            if (mc.player.getCooledAttackStrength(-delay.getValue()) == 1.0F) {
                float[] facingRotations = PlayerUtil.getPlayerFacingRotations(target.posX, target.posY + target.height/2, target.posZ);

                mc.player.connection.sendPacket(new CPacketUseEntity(target));
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(
                        mc.player.posX, mc.player.posY, mc.player.posZ,
                        facingRotations[1], facingRotations[0], mc.player.onGround
                ));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.resetCooldown();

                event.setCanceled(true);
            }
        }
    }

    public void onAttackEntity(AttackEntityEvent event) {
        lastAttackedEntity = event.getEntity();
    }

    public boolean isValidTarget(Entity entity) {

        // Stop the player from attacking themselves
        if (entity == mc.player || entity == mc.getRenderViewEntity()) {
            return false;
        }

        if (mc.player.getDistanceSq(entity) <= Math.pow(reach.getValue(), 2)) {

            if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0.0F) {

                if (entity instanceof EntityPlayer) {
                    if (players.getValue()) {
                        if (targetFriends.getValue()) {
                            return true;
                        }

                        return !GateClient.getGate().FRIENDS_MANAGER.isFriend(entity.getName());
                    }

                    return false;
                }

                if (entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
                    return monsters.getValue();
                }

                return passives.getValue();

            }
        }

        return false;
    }

    public enum Priority {
        CLOSEST("Closest"),
        FOCUS("Focus");

        private final String name;
        Priority(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
