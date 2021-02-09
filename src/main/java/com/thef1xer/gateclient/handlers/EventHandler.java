package com.thef1xer.gateclient.handlers;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
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
                for (Module module : GateClient.gateClient.moduleManager.moduleList) {
                    if (Keyboard.getEventKey() == module.getKeyBind()) {
                        module.toggle();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onMessageSent(ClientChatEvent event) {
        String message = event.getOriginalMessage();

        //Commands
        if (message.startsWith(GateClient.gateClient.commandManager.prefix)) {
            String[] args = message.substring(GateClient.gateClient.commandManager.prefix.length()).split(" ");
            boolean found = false;

            for (Command command: GateClient.gateClient.commandManager.commandList) {
                if (ChatUtil.isCommand(args[0], command)) {
                    command.onCommand(args);
                    GateClient.gateClient.configManager.save();
                    GateClient.gateClient.presetManager.saveActivePreset(GateClient.gateClient.configManager.activePreset);
                    found = true;
                }
            }

            if (!found) {
                ChatUtil.clientMessage("Command Not Found");
            }

            event.setCanceled(true);
        }
    }
}
