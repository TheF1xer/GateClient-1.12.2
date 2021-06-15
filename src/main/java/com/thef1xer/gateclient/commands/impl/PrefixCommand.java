package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix", "Changes the Command Prefix", "prefix <new prefix>");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {
            GateClient.getGate().commandManager.setPrefix(args[1]);
            GateClient.getGate().configManager.save();
            ChatUtil.clientMessage("Prefix changed to: " + TextFormatting.GOLD + args[1]);
            return;
        }

        this.syntaxError();
    }
}
