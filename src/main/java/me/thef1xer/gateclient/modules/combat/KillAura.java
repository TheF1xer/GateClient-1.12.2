package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.events.SendPacketEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KillAura extends Module {
    public static final KillAura INSTANCE = new KillAura();

    public final EnumSetting priority = new EnumSetting("Priority", "priority", Priority.values(), Priority.CLOSEST);
    public final FloatSetting reach = new FloatSetting("Reach", "reach", 3F, 0F, 6F);
    public final FloatSetting delay = new FloatSetting("Added Delay" , "delay", 0F);
    public final BooleanSetting players = new BooleanSetting("Players", "players", true);
    public final BooleanSetting monsters = new BooleanSetting("Monsters", "monsters", true);
    public final BooleanSetting passives = new BooleanSetting("Passives", "passives", false);

    private Entity target;
    private Entity focusTarget;
    private final Minecraft mc = Minecraft.getMinecraft();

    public KillAura() {
        super("Kill Aura", "killaura", Module.ModuleCategory.COMBAT);
        this.addSettings(priority, reach, delay, players, monsters, passives);
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
        target = null;

        if (mc.world == null || !mc.world.isRemote) {
            return;
        }

        // Select target depending on the mode
        if (priority.getCurrentValue() == Priority.CLOSEST) {

            // Select closest entity
            for (Entity entity : mc.world.loadedEntityList) {
                if (isValidTarget(entity)) {
                    if (target == null) {
                        target = entity;
                    } else if (mc.player.getDistanceSq(entity) < mc.player.getDistanceSq(target)) {
                        target = entity;
                    }
                }
            }
        } else if (priority.getCurrentValue() == Priority.FOCUS) {
            if (focusTarget != null) {

                // Check if the last entity targeted is still loaded
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity == focusTarget) {
                        if (isValidTarget(entity)) {
                            target = entity;
                        }
                    }
                }
            }
        }

        // Attack the target
        if (target != null) {
            if (mc.player.getCooledAttackStrength(-delay.getValue()) == 1.0F) {
                mc.player.connection.sendPacket(new CPacketUseEntity(target));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.resetCooldown();
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent event) {

        // Select target if mode is Focus
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();

            if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                this.focusTarget = packet.getEntityFromWorld(mc.world);
            }
        }

        // Rotate the player towards the target
        if (target != null) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer packet = (CPacketPlayer) event.getPacket();

                float[] facingRotations = PlayerUtil.getPlayerFacingRotations(target.posX, target.posY + target.height/2, target.posZ);

                packet.pitch = facingRotations[0];
                packet.yaw = facingRotations[1];
            }
        }
    }

    public boolean isValidTarget(Entity entity) {

        // Stop the player from attacking themselves
        if (entity == mc.player || entity == mc.getRenderViewEntity()) {
            return false;
        }

        if (mc.player.getDistanceSq(entity) <= Math.pow(reach.getValue(), 2)) {

            if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0.0F) {

                if (entity instanceof EntityPlayer) {
                    return players.getValue();
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
