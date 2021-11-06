package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow extends Module {
    public static final NoSlow INSTANCE = new NoSlow();

    public final BooleanSetting sneak = new BooleanSetting("Sneaking", "sneak", true);
    public final BooleanSetting item = new BooleanSetting("Item", "item", true);

    public NoSlow() {
        super("NoSlow", "noslow", Module.ModuleCategory.MOVEMENT);
        this.addSettings(sneak, item);
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
    public void onInput(InputUpdateEvent event) {
        if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            if (item.getValue() && Minecraft.getMinecraft().player.isHandActive() && !Minecraft.getMinecraft().player.isRiding()) {
                Minecraft.getMinecraft().player.movementInput.moveStrafe /= 0.2F;
                Minecraft.getMinecraft().player.movementInput.moveForward /= 0.2F;
            }

            if (sneak.getValue() && Minecraft.getMinecraft().player.isSneaking()) {
                Minecraft.getMinecraft().player.movementInput.moveStrafe = (float) ((double) Minecraft.getMinecraft().player.movementInput.moveStrafe / 0.3D);
                Minecraft.getMinecraft().player.movementInput.moveForward = (float) ((double) Minecraft.getMinecraft().player.movementInput.moveForward / 0.3D);
            }
        }
    }
}
