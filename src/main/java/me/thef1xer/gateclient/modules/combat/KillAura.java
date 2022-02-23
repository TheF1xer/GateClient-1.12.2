package me.thef1xer.gateclient.modules.combat;

import me.thef1xer.gateclient.events.SendPacketEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
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

        // Select target if mode if Focus
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                this.focusTarget = packet.getEntityFromWorld(mc.world);
            }
        }

        // Send Rotation
        if (target != null) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer packet = (CPacketPlayer) event.getPacket();

                double deltaX = target.posX - mc.player.posX;
                double deltaY = target.posY + target.height/2 - mc.player.posY - mc.player.getEyeHeight();
                double deltaZ = target.posZ - mc.player.posZ;
                double deltaGround = Math.sqrt(deltaX*deltaX + deltaZ*deltaZ);

                float pitch = (float) - Math.toDegrees(Math.atan(deltaY/deltaGround));
                float yaw = (float) - Math.toDegrees(Math.atan(deltaX/deltaZ));

                // Yaw in Minecraft is weird and this is the only thing I could make to fix it
                if (deltaZ <= 0) {
                    if (deltaX > 0) {
                        yaw = yaw - 180F;
                    } else {
                        yaw = yaw + 180F;
                    }
                }
                packet.yaw = yaw;
                packet.pitch = pitch;
            }
        }
    }

    public boolean isValidTarget(Entity entity) {
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
