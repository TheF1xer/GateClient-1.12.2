package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;

public class NoSlow extends Module {
    public static final NoSlow INSTANCE = new NoSlow();

    public final BooleanSetting sneak = new BooleanSetting("Sneaking", "sneak", true);
    public final BooleanSetting item = new BooleanSetting("Item", "item", true);

    public NoSlow() {
        super("NoSlow", "noslow", Module.ModuleCategory.MOVEMENT);
        this.addSettings(sneak, item);
    }

    public void onInputUpdate() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null && mc.player != null) {

            // Cancel the effects of using an item
            if (item.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
                mc.player.movementInput.moveStrafe /= 0.2F;
                mc.player.movementInput.moveForward /= 0.2F;
            }

            // Cancel the effects of sneaking
            if (sneak.getValue() && mc.player.isSneaking()) {
                mc.player.movementInput.moveStrafe = (float) ((double) mc.player.movementInput.moveStrafe / 0.3D);
                mc.player.movementInput.moveForward = (float) ((double) mc.player.movementInput.moveForward / 0.3D);
            }
        }
    }
}
