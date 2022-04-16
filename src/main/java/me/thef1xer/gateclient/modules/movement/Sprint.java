package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;

public class Sprint extends Module {
    public static final Sprint INSTANCE = new Sprint();

    public Sprint() {
        super("Sprint", "sprint", Module.ModuleCategory.MOVEMENT);
    }

    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null && mc.world.isRemote) {
            if (mc.player != null &&
                    mc.player.moveForward > 0F &&
                    !mc.player.collidedHorizontally &&
                    !mc.player.isSprinting() &&
                    !mc.player.isSneaking()) {

                mc.player.setSprinting(true);
            }
        }
    }
}
