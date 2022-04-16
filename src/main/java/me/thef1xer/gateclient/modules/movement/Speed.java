package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.PlayerMoveEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;

public class Speed extends Module {
    public static final Speed INSTANCE = new Speed();

    private final Minecraft mc = Minecraft.getMinecraft();

    public Speed() {
        super("Speed", "speed", Module.ModuleCategory.MOVEMENT);
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        // Max Velocity = 0.2863436192274094047655691820943506607929515418502202643171
        // Just Strafe Mode available

        if (mc.player.isSneaking() || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.player.isElytraFlying() || mc.player.capabilities.isFlying) {
            return;
        }

        // Max speed calculated with the source code (rounded up)
        float playerSpeed = 0.28634362F;

        double[] moveVec = PlayerUtil.getPlayerMoveVec();

        event.x = playerSpeed * moveVec[0];
        event.z = playerSpeed * moveVec[1];
    }
}
