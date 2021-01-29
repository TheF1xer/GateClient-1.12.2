package com.thef1xer.gateclient.commands;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;

public class HelpCommand extends Command{
    public HelpCommand() {
        super("help", "Displays this list", "help", new String[]{"?"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            ChatUtil.clientMessage("List of Commands in this client:");
            for (Command command : GateClient.gateClient.commandManager.commandList) {
                ChatUtil.userMessage(TextFormatting.AQUA + command.getName() + ": " + TextFormatting.RESET + command.getDesc());
            }
        } else {
            this.syntaxError();
        }
    }
}
