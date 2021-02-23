package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Displays this list", "help", "?");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            ChatUtil.clientMessage("List of Commands in this client:");
            for (Command command : GateClient.gate.commandManager.commandList) {
                ChatUtil.clientMessage(TextFormatting.GOLD + command.getName() + ": " + TextFormatting.RESET + command.getDesc());
            }
        } else {
            this.syntaxError();
        }
    }
}
