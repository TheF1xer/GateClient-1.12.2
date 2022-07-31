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
import org.lwjgl.input.Keyboard;

public class CommonEventHandler {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final GateClient gate = GateClient.getGate();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated() && mc.world != null && mc.player != null) {
            if (Keyboard.getEventKeyState()) {

                for (Module module : gate.MODULE_MANAGER.MODULE_LIST) {
                    if (Keyboard.getEventKey() == module.getKeyBind()) {

                        module.toggle();
                        if (gate.PRESET_MANAGER.isAutoSave()) {
                            gate.PRESET_MANAGER.saveActivePreset();
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
        if (message.startsWith(gate.COMMAND_MANAGER.getPrefix())) {
            mc.ingameGUI.getChatGUI().addToSentMessages(message);
            String[] args = message.substring(gate.COMMAND_MANAGER.getPrefix().length()).split(" ");
            boolean found = false;

            for (Command command: gate.COMMAND_MANAGER.COMMAND_LIST) {
                if (ChatUtil.isCommand(args[0], command)) {

                    if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
                        ChatUtil.clientMessage("Syntax for " + TextFormatting.GOLD + command.getName() + TextFormatting.RESET + " command:");
                        for (String syntax : command.getSyntax()) {
                            ChatUtil.clientMessage(TextFormatting.YELLOW + gate.COMMAND_MANAGER.getPrefix() + syntax);
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
