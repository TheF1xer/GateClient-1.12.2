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
                for (Module module : GateClient.gate.moduleManager.MODULE_LIST) {
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
        if (message.startsWith(GateClient.gate.commandManager.prefix)) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
            String[] args = message.substring(GateClient.gate.commandManager.prefix.length()).split(" ");
            boolean found = false;

            for (Command command: GateClient.gate.commandManager.COMMAND_LIST) {
                if (ChatUtil.isCommand(args[0], command)) {
                    if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
                        ChatUtil.clientMessage("Syntax for " + TextFormatting.GOLD + command.getName() + TextFormatting.RESET + " command:");
                        for (String syntax : command.getSyntax()) {
                            ChatUtil.clientMessage(TextFormatting.YELLOW + GateClient.gate.commandManager.prefix + syntax);
                        }
                    } else {
                        command.onCommand(args);
                        GateClient.gate.configManager.save();
                        GateClient.gate.presetManager.saveActivePreset();
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
