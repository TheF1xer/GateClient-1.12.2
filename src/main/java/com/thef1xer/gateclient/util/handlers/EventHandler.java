package com.thef1xer.gateclient.util.handlers;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class EventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated() && Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            if (Keyboard.getEventKeyState()) {
                for (Module module : GateClient.moduleManager.moduleList) {
                    if (Keyboard.getEventKey() == module.getKeyBind()) {
                        module.toggle();
                    }
                }
            }
        }
    }
}
