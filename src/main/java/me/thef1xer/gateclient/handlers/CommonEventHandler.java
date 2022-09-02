package me.thef1xer.gateclient.handlers;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class CommonEventHandler {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final GateClient gate = GateClient.getGate();


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!gate.threadManager.clientThread.isAlive()) {
            ChatUtil.clientMessage("An error occurred with the Client Thread, please restart your Client");
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated() && mc.world != null && mc.player != null) {
            if (Keyboard.getEventKeyState()) {

                for (Module module : gate.moduleManager.moduleList) {
                    if (Keyboard.getEventKey() == module.getKeyBind()) {

                        module.toggle();
                        if (gate.presetManager.isAutoSave()) {
                            gate.presetManager.saveActivePreset();
                        }
                    }
                }

            }

        }
    }

    @SubscribeEvent
    public void onClientChat(ClientChatEvent event) {
        String message = event.getOriginalMessage();

        // Commands
        if (message.startsWith(gate.commandManager.getPrefix())) {
            mc.ingameGUI.getChatGUI().addToSentMessages(message);
            String[] args = message.substring(gate.commandManager.getPrefix().length()).split(" ");
            boolean found = false;

            for (Command command: gate.commandManager.COMMAND_LIST) {
                if (ChatUtil.isCommand(args[0], command)) {

                    if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
                        ChatUtil.clientMessage("Syntax for " + TextFormatting.GOLD + command.getName() + TextFormatting.RESET + " command:");
                        for (String syntax : command.getSyntax()) {
                            ChatUtil.clientMessage(TextFormatting.YELLOW + gate.commandManager.getPrefix() + syntax);
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
