package com.thef1xer.gateclient.modules.combat;

import com.thef1xer.gateclient.events.SendPacketEvent;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.EnumSetting;
import com.thef1xer.gateclient.settings.impl.FloatSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KillAura extends Module {
    public static KillAura INSTANCE;

    private Entity target;
    private Entity focusTarget;

    public final EnumSetting<Priority> priority = new EnumSetting<>("Target Priority", "priority", Priority.values(), Priority.CLOSEST);
    public final FloatSetting reach = new FloatSetting("Reach", "reach", 3F, 0F, 6F);
    public final FloatSetting delay = new FloatSetting("Added Delay" , "delay", 0F, -100F, 100F);

    public KillAura() {
        super("Kill Aura", "killaura", EnumModuleCategory.COMBAT);
        this.addSettings(priority, reach, delay);

        KillAura.INSTANCE = this;
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent.ClientTickEvent event) {
        target = null;
        if (Minecraft.getMinecraft().world == null || !Minecraft.getMinecraft().world.isRemote) {
            return;
        }

        if (priority.getCurrentValue() == Priority.CLOSEST) {
            for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                if (isTarget(entity)) {
                    if (target == null) {
                        target = entity;
                    } else if (Minecraft.getMinecraft().player.getDistanceSq(entity) < Minecraft.getMinecraft().player.getDistanceSq(target)) {
                        target = entity;
                    }
                }
            }
        } else if (priority.getCurrentValue() == Priority.FOCUS) {
            if (focusTarget != null) {
                for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (entity == focusTarget) {
                        if (isTarget(entity)) {
                            target = entity;
                        }
                    }
                }
            }
        }

        if (target != null) {
            if (Minecraft.getMinecraft().player.getCooledAttackStrength(-delay.getValue()) == 1.0F) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketUseEntity(target));
                Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.resetCooldown();
            }
        }
    }

    @SubscribeEvent
    public void onPacketEvent(SendPacketEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                this.focusTarget = packet.getEntityFromWorld(Minecraft.getMinecraft().world);
            }
        }

        if (target != null) {
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation || event.getPacket() instanceof CPacketPlayer.Rotation) {
                double deltaX = target.posX - Minecraft.getMinecraft().player.posX;
                double deltaY = target.posY + target.height/2 - Minecraft.getMinecraft().player.posY - Minecraft.getMinecraft().player.getEyeHeight();
                double deltaZ = target.posZ - Minecraft.getMinecraft().player.posZ;
                double deltaGround = Math.sqrt(deltaX*deltaX + deltaZ*deltaZ);

                float pitch = (float) - Math.toDegrees(Math.atan(deltaY/deltaGround));
                float yaw = (float) - Math.toDegrees(Math.atan(deltaX/deltaZ));

                //Yaw in Minecraft is weird and this is the only thing I could make to fix it
                if (deltaZ <= 0) {
                    if (deltaX > 0) {
                        yaw = yaw - 180F;
                    } else {
                        yaw = yaw + 180F;
                    }
                }
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                event.setPacket(new CPacketPlayer.PositionRotation(player.posX, player.posY, player.posZ, yaw, pitch, player.onGround));
            }
        }
    }

    public boolean isTarget(Entity entity) {
        return entity != Minecraft.getMinecraft().player &&
                entity != Minecraft.getMinecraft().getRenderViewEntity() &&
                Minecraft.getMinecraft().player.getDistanceSq(entity) <= Math.pow(reach.getValue(), 2) &&
                entity instanceof EntityLivingBase &&
                ((EntityLivingBase) entity).getHealth() > 0.0F;
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
