package com.thef1xer.gateclient.modules.combat;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KillAura extends Module {

    public KillAura() {
        super("Kill Aura", EnumModuleCategory.COMBAT, Keyboard.KEY_Z);
    }

    @Override
    public void onEnabled() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisabled() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent.ClientTickEvent event) {

        Entity target = null;
        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (isTarget(entity)) {
                if (target == null) {
                    target = entity;
                } else if (Minecraft.getMinecraft().player.getDistanceSq(entity) < Minecraft.getMinecraft().player.getDistanceSq(target)) {
                    target = entity;
                }
            }
        }

        if (target != null) {
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

            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, Minecraft.getMinecraft().player.onGround));

            if (Minecraft.getMinecraft().player.getCooledAttackStrength(0.5F) == 1.0F) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketUseEntity(target));
                Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.resetCooldown();
            }
        }
    }

    public boolean isTarget(Entity entity) {
        return entity != Minecraft.getMinecraft().player &&
                Minecraft.getMinecraft().player.getDistanceSq(entity) < 36.0D &&
                entity instanceof EntityLiving &&
                ((EntityLiving) entity).getHealth() > 0.0F;
    }
}
