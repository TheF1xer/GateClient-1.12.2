package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint extends Module {
    public static final Sprint INSTANCE = new Sprint();

    public Sprint() {
        super("Sprint", "sprint", Module.ModuleCategory.MOVEMENT);
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
    public void onLivingEvent(LivingEvent.LivingUpdateEvent event) {
        if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().world.isRemote) {
            if (Minecraft.getMinecraft().player != null &&
                    Minecraft.getMinecraft().player.moveForward > 0F &&
                    !Minecraft.getMinecraft().player.collidedHorizontally &&
                    !Minecraft.getMinecraft().player.isSprinting() &&
                    !Minecraft.getMinecraft().player.isSneaking()) {
                Minecraft.getMinecraft().player.setSprinting(true);
            }
        }
    }
}
