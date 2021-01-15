package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", EnumModuleCategory.MOVEMENT, Keyboard.KEY_M);
    }

    @Override
    public void onEnabled() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("enabled");
    }

    @Override
    public void onDisabled() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onLivingEvent(LivingEvent.LivingUpdateEvent event) {
        if (Minecraft.getMinecraft().world.isRemote) {
            if (Minecraft.getMinecraft().player.moveForward > 0F &&
                    !Minecraft.getMinecraft().player.collidedHorizontally &&
                    !Minecraft.getMinecraft().player.isSprinting() &&
                    !Minecraft.getMinecraft().player.isSneaking()) {
                Minecraft.getMinecraft().player.setSprinting(true);
            }
        }
    }
}
