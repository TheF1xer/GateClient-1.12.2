package com.thef1xer.gateclient.handlers;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
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
                for (Module module : GateClient.getGate().moduleManager.MODULE_LIST) {
                    if (Keyboard.getEventKey() == module.getKeyBind()) {
                        module.toggle();
                        GateClient.getGate().presetManager.saveActivePreset();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onMessageSent(ClientChatEvent event) {
        String message = event.getOriginalMessage();

        //Commands
        if (message.startsWith(GateClient.getGate().commandManager.getPrefix())) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
            String[] args = message.substring(GateClient.getGate().commandManager.getPrefix().length()).split(" ");
            boolean found = false;

            for (Command command: GateClient.getGate().commandManager.COMMAND_LIST) {
                if (ChatUtil.isCommand(args[0], command)) {
                    if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
                        ChatUtil.clientMessage("Syntax for " + TextFormatting.GOLD + command.getName() + TextFormatting.RESET + " command:");
                        for (String syntax : command.getSyntax()) {
                            ChatUtil.clientMessage(TextFormatting.YELLOW + GateClient.getGate().commandManager.getPrefix() + syntax);
                        }
                    } else {
                        command.onCommand(args);
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                ChatUtil.clientMessage("Command Not Found");
            }

            event.setCanceled(true);
        }
    }
}
