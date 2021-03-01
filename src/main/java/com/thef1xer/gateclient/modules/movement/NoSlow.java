package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow extends Module {
    public static final NoSlow INSTANCE = new NoSlow();

    public final BooleanSetting sneak = new BooleanSetting("Don't slow while Sneaking", "sneak", true);
    public final BooleanSetting item = new BooleanSetting("Don't slow while using items (food, shields, etc.)", "item", true);

    public NoSlow() {
        super("NoSlow", "noslow", EnumModuleCategory.MOVEMENT);
        this.addSettings(sneak, item);
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
    public void onTick(InputUpdateEvent event) {
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
