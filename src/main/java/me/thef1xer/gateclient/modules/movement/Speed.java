package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.PlayerMoveEvent;
import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speed extends Module {
    public static final Speed INSTANCE = new Speed();

    public Speed() {
        super("Speed", "speed", Module.ModuleCategory.MOVEMENT);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        super.onDisable();
    }

    //Priority must be HIGHEST because this must be the first speed change made
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        //Max Velocity = 0.2863436192274094047655691820943506607929515418502202643171
        //Just Strafe Mode available

        if (Minecraft.getMinecraft().player.isSneaking() || Minecraft.getMinecraft().player.isInWater() || Minecraft.getMinecraft().player.isInLava() || Minecraft.getMinecraft().player.isOnLadder() || Minecraft.getMinecraft().player.isElytraFlying() || Minecraft.getMinecraft().player.capabilities.isFlying) {
            return;
        }

        //Max speed calculated with the source code (rounded up)
        float playerSpeed = 0.28634362F;
        float forward = Minecraft.getMinecraft().player.moveForward;
        float strafe = Minecraft.getMinecraft().player.moveStrafing;

        if (forward == 0 && strafe == 0) {
            event.x = 0;
            event.z = 0;
        } else {
            float yaw = Minecraft.getMinecraft().player.rotationYaw;

            //Change yaw depending on the direction the playing is travelling
            if (forward > 0) {

                if (strafe > 0) {
                    yaw = yaw - 45;
                } else if (strafe < 0) {
                    yaw = yaw + 45;
                }

            } else if (forward < 0) {

                yaw = yaw - 180;
                if (strafe > 0) {
                    yaw = yaw + 45;
                } else if (strafe < 0) {
                    yaw = yaw - 45;
                }

            } else {

                if (strafe > 0) {
                    yaw = yaw - 90;
                } else if (strafe < 0) {
                    yaw = yaw + 90;
                }

            }

            event.x = -playerSpeed * Math.sin(Math.toRadians(yaw));
            event.z = playerSpeed * Math.cos(Math.toRadians(yaw));
        }
    }
}
