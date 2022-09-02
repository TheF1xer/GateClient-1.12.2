package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Displays this list", "help");
        this.addAliases("?");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            ChatUtil.clientMessage(TextFormatting.BOLD + "List of Commands in this Client:");
            for (Command command : GateClient.getGate().commandManager.COMMAND_LIST) {
                ChatUtil.clientMessage(TextFormatting.GOLD + command.getName() + ": " + TextFormatting.RESET + command.getDesc());
            }
        } else {
            this.syntaxError();
        }
    }
}
