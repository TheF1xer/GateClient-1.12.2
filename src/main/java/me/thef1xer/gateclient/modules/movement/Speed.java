package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.PlayerMoveEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speed extends Module {
    public static final Speed INSTANCE = new Speed();

    private final Minecraft mc = Minecraft.getMinecraft();

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

    // Priority must be HIGHEST because this must be the first speed change made
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
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
