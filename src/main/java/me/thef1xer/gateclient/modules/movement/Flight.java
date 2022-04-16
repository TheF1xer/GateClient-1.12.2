package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.PlayerMoveEvent;
import me.thef1xer.gateclient.events.UpdateWalkingPlayerEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;

public class Flight extends Module {
    public static final Flight INSTANCE = new Flight();

    public final EnumSetting mode = new EnumSetting("Mode", "mode", Mode.values(), Mode.VANILLA);

    private final Minecraft mc = Minecraft.getMinecraft();

    public Flight() {
        super("Flight", "flight", ModuleCategory.MOVEMENT);
        addSettings(mode);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.capabilities.isFlying = false;
        }
    }

    public void onClientTick() {
        if (mc.player != null) {
            mc.player.capabilities.isFlying = true;
        }
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        if (mode.getCurrentValue() == Mode.PACKET) {

            // Cancel the movement, it will be calculated later
            event.x = 0;
            event.y = 0;
            event.z = 0;
        }
    }

    public void onUpdateWalkingPlayer() {
        if (mode.getCurrentValue() == Mode.PACKET) {
            double[] moveVec = PlayerUtil.getPlayerMoveVec();

            // Calculate the movement using the Movement Vector
            double speedX = moveVec[0] * 0.2D;
            double speedZ = moveVec[1] * 0.2D;
            double speedY = 0;

            if (mc.player.movementInput.jump) {
                speedY = 0.2D;
            } else if (mc.player.movementInput.sneak) {
                speedY = -0.2D;
            }

            // Set new position before sending the packet
            mc.player.setPosition(mc.player.posX + speedX,
                    mc.player.posY + speedY,
                    mc.player.posZ + speedZ);

            mc.player.setVelocity(0, 0, 0);
        }
    }

    public enum Mode {
        VANILLA("Vanilla"),
        PACKET("Packet");

        private final String name;
        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
